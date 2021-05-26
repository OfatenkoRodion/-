package model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ElasticSearchBuildRequest(from: Int, size: Int, _source: Set[String])

object ElasticSearchBuildRequest {
  implicit val elasticSearchBuildRequestConfDecoder: Decoder[ElasticSearchBuildRequest] = deriveDecoder
  implicit val elasticSearchBuildRequestConfEncoder: Encoder[ElasticSearchBuildRequest] = deriveEncoder
}