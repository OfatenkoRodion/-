import java.io.{ByteArrayOutputStream, InputStream}
import java.lang.annotation.Annotation
import java.nio.charset.StandardCharsets

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes, Uri}
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import configuretion.CircleServiceConfiguration
import controller.CircleController
import io.swagger.annotations.Api
import io.swagger.models.{Info, Scheme, SecurityRequirement, Swagger}
import org.webjars.WebJarAssetLocator
import service.{CircleService, CircleServiceImpl}
import util.Controller

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import io.swagger.util.{Json => SwaggerJson}
import org.reflections.Reflections

import scala.collection.JavaConverters._
import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.ReaderConfig
import io.swagger.models.auth.{ApiKeyAuthDefinition, SecuritySchemeDefinition}
import io.swagger.models.auth.In.QUERY

object Main extends App {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val circleServiceConfiguration = CircleServiceConfiguration() // TODO make separate module for configurations
  val circleService: CircleService = wire[CircleServiceImpl]  // TODO make separate module for services
  val circleController: CircleController = wire[CircleController]

  val controllers = Seq(
    circleController
  )

  val swagger: Controller = new Controller {

    val clientId = "localhost:8080"
    val tagFilter = "MATH"
    val basePath = "/api/v1"
    val packages = Seq("controller")


    val security: Seq[Map[String, Seq[String]]] = Seq(Map("apiKey" -> Seq("pass")))
    val sec = security.map { m =>
      m.map { case (key, value) => key -> value.asJava }.asJava
    }


    override def route: Route = {
      pathPrefix("swagger") {
        pathEndOrSingleSlash {
          redirectToIndexWithJson()
        } ~
          path("index.html")(serveIndex()) ~
          extractUnmatchedPath { path =>
            serveSwaggerUi(path.toString)
          } ~
          (get & path("swagger.json") & parameter('basePath.?)) { basePathOpt =>
            val correctedPathOpt = basePathOpt.map(addMissingSlash)
            swaggerJson(clientId, correctedPathOpt)
          }
      }
    }

    private def serveIndex() = {
      val res = getSwaggerIndexPageAsString(clientId)
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, res))
    }

    private def serveSwaggerUi(path: String) =
      Try(webJarAssetLocator.getFullPath(path)) match {
        case Success(fullPath) =>
          getFromResource(fullPath)
        case Failure(_: IllegalArgumentException) =>
          reject
        case Failure(e) =>
          failWith(e)
      }

    private def addMissingSlash(path: String): String = {
      if (path.startsWith("/")) path
      else "/" + path
    }

    private final def redirectToIndexWithJson(): Route =
      extract(_.request.uri.path) { path =>
        val newPath = path.toString.stripSuffix("/") + "/index.html"
        val newUrl = Uri(newPath).withQuery(Query("url" -> "swagger.json"))

        redirect(newUrl, StatusCodes.SeeOther)
      }

    private def swaggerJson(host: String, basePathOpt: Option[String]): Route = {
      onSuccess(getScheme(host, basePathOpt)) { json =>
        complete(json)
      }
    }

    private def getSwaggerIndexPageAsString(clientId: String): String = {
      val url = this.getClass.getClassLoader.getResource("swagger/index.html")
      val is = url.openStream()
      val bytes = try readByteArray(is) finally is.close()
      new String(bytes, StandardCharsets.UTF_8.name()).replaceAll("\\$clientId", clientId)
    }

    private def readByteArray(is: InputStream): Array[Byte] = {
      val os = new ByteArrayOutputStream()
      val buffer = new Array[Byte](1024)
      Iterator
        .continually(is.read(buffer))
        .takeWhile(_ != -1)
        .foreach(readCount => os.write(buffer, 0, readCount))
      os.close()
      os.toByteArray
    }

    private lazy val webJarAssetLocator = new WebJarAssetLocator

    private def getScheme(host: String, rewriteBasePathOpt: Option[String]): Future[String] = {
      schemeFuture(host).map { swagger =>
        this.synchronized {

          rewriteBasePathOpt match {
            case Some(newBasePath) =>
              swagger
                .host(host)
                .setBasePath(newBasePath)
              val resultString = SwaggerJson.mapper().writeValueAsString(swagger)
              swagger.setBasePath(this.basePath)
              resultString
            case _ => SwaggerJson.mapper().writeValueAsString(swagger)
          }

        }
      }

    }

    private lazy val schemeFuture: String => Future[Swagger] = host => Future(generateSchemeForClasses(listApiClasses, host))

    private def generateSchemeForClasses(apiClasses: Seq[Class[_]], host: String): Swagger = {
      run(apiClasses.toSet, host)
    }

    private def run(serviceClasses: Set[Class[_]], host: String): Swagger = {

      val readerConfig = new ReaderConfig {
        def getIgnoredRoutes: java.util.Collection[String] = Set.empty[String].asJava

        def isScanAllResources: Boolean = false
      }

      def configSwagger: Swagger = {
        val swagger = new Swagger()
          .basePath(basePath)
          .schemes(Seq(Scheme.HTTP).asJava)
          .host(host)

        security.foreach { m =>
          m.foreach { case (key, value) =>
            val sr = new SecurityRequirement
            sr.requirement(key, value.asJava)
            swagger.addSecurity(sr)
          }
        }
        //postProcess(swagger)



        swagger
      }

      val swagger = new Reader(configSwagger, readerConfig).read(serviceClasses.asJava)
      swagger
    }

    private def postProcess(swagger: Swagger): Swagger = {

      val sec = security.map { m =>
        m.map { case (key, value) => key -> value.asJava }.asJava
      }

      if (sec.nonEmpty) {
        for {
          path <- swagger.getPaths.values.asScala
          operation <- path.getOperationMap.values.asScala
        } yield {
          operation.setSecurity(sec.asJava)
        }
      }
      swagger
    }

    private def listApiClasses: Seq[Class[_]] = {

      def findApiAnnotation(cls: Class[_]): Option[Annotation] = cls.getAnnotations
        .find(_.getClass.getInterfaces.exists(_.getCanonicalName == classOf[Api].getCanonicalName))

      def getAnnotationValue(a: Annotation): String = {
        Try(a.getClass.getDeclaredMethod("value").invoke(a)) match {
          case Success(annotationValue) =>
            annotationValue.toString
          case Failure(e) =>
            throw e
        }
      }

      def findTags(value: String): Seq[String] = subtagPattern.findAllIn(value).matchData.toSeq.map(_.group(1).toLowerCase)

      /*val serviceClasses = */ packages.flatMap(new Reflections(_).getTypesAnnotatedWith(classOf[Api]).asScala.toSet)
     /* val validTagsSeq = tagFilter.toSeq
      serviceClasses.filter { serviceClass =>
        findApiAnnotation(serviceClass).exists { annotation =>
          validTagsSeq.exists(findTags(getAnnotationValue(annotation)).contains)
        }
      }*/


    }


    private val subtagPattern = """\[\s*(.+)\s*\]""".r

  }

  private val routes: Route =
      pathPrefix("api" / "v1") {
        circleController.route ~
          path("hello") {
            get {
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
            }
          }
      } ~ swagger.route

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)



  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
