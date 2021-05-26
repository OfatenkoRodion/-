package client

import com.redis.RedisClient
import io.circe.parser._
import io.circe.{Decoder, Encoder}

import java.nio.charset.StandardCharsets
import scala.concurrent.{ExecutionContext, Future}

class RedisClientDemoImpl(host: String, port: Int)
                         (implicit executionContext: ExecutionContext) extends RedisClient(host, port) {

  import com.redis.serialization.Parse.Implicits.parseByteArray

  def setObject[T](key: String, value: T)(implicit encoder: Encoder[T]): Future[Boolean] = {
    println("key insert" + key)
    Future {
      val resByte = encoder.apply(value).toString().getBytes(StandardCharsets.UTF_8)
      set(key, resByte)
    }
  }

  def getObj[T](key: String)(implicit decoder: Decoder[T]): Future[Option[T]] = {
    println("key get" + key)
    Future {
      get[Array[Byte]](key).map { v =>
        parse(new String(v, StandardCharsets.UTF_8)) match {
          case Left(value) => throw new RuntimeException(value.message)
          case Right(value) => value.as[T].getOrElse(throw new RuntimeException("Mission failed!"))
        }
      }
    }
  }

}
