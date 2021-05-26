package model

import io.circe._
import io.circe.generic.semiauto._

case class ElasticSearchConf(configId: Int, elasticSearchFields: ElasticSearchFields)

object ElasticSearchConf {
  implicit val elasticSearchConfDecoder: Decoder[ElasticSearchConf] = deriveDecoder
  implicit val elasticSearchConfEncoder: Encoder[ElasticSearchConf] = deriveEncoder
}

case class ElasticSearchFieldsDTO(from: Int, size: Int, _source: Set[String])

object ElasticSearchFieldsDTO {
  implicit val ElasticSearchFieldsDTOConfDecoder: Decoder[ElasticSearchFieldsDTO] = deriveDecoder
  implicit val ElasticSearchFieldsDTOConfEncoder: Encoder[ElasticSearchFieldsDTO] = deriveEncoder
}

case class ElasticSearchFields(_source: Set[String])

object ElasticSearchFields {
  implicit val elasticSearchConfDecoder: Decoder[ElasticSearchFields] = deriveDecoder
  implicit val elasticSearchConfEncoder: Encoder[ElasticSearchFields] = deriveEncoder
}