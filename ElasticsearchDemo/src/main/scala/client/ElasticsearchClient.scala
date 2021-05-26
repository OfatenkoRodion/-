package client

import model.{ElasticSearchBuildRequest, ElasticSearchConf}
import org.elasticsearch.client.{Request, RestClient}

import scala.concurrent.{ExecutionContext, Future}

trait ElasticsearchClient {
  def createIndex(inxName: String, path: String)

  def insert(inxName: String, path: String, json: String)

  def refresh(inxName: String)

  def viewData(inxName: String, path: String, conf: ElasticSearchBuildRequest): Future[String]

  def deleteIndex(inxName: String)
}

class ElasticsearchClientImpl(client: RestClient)
                             (implicit ec: ExecutionContext) extends ElasticsearchClient {

  def createIndex(inxName: String, path: String) = {
    val request = new Request(
      "PUT",
      s"/$inxName")
    // TODO make this configurable
    request.setJsonEntity(
      s"""
         |{
         |    "settings": {
         |        "index.number_of_shards" : 1,
         |        "index.number_of_replicas": 0
         |    },
         |    "mappings": {
         |        "$path": {
         |            "properties": {
         |                "item_id": {"type": "text"},
         |                "name": {"type": "text"},
         |                "locale": {"type": "text"},
         |                "click": {"type": "integer"},
         |                "purchase": {"type": "integer"}
         |            }
         |        }
         |    }
         |
         |}
         |""".stripMargin)
    client.performRequest(request)
  }

  def insert(inxName: String, path: String, json: String) = {
    val request = new Request(
      "POST",
      s"/$inxName/$path")
    request.setJsonEntity(json)
    client.performRequest(request);
  }

  def refresh(inxName: String) = {
    val request = new Request(
      "POST",
      s"/$inxName/_refresh")
    client.performRequest(request);
  }

  def viewData(inxName: String, path: String, conf: ElasticSearchBuildRequest): Future[String] = {
    val request = new Request(
      "POST",
      s"/$inxName/$path/_search")

    import io.circe.syntax._
    request.setJsonEntity(conf.asJson.toString())

    Future(client.performRequest(request)).map(response =>
      scala.io.Source.fromInputStream(response.getEntity.getContent).mkString
    )
  }

  def deleteIndex(inxName: String) = {
    val request = new Request(
      "DELETE",
      s"/$inxName")
    client.performRequest(request);
  }
}
