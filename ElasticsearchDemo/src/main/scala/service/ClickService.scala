package service

import client.ElasticsearchClient
import model.{ElasticSearchConf, ElasticSearchFields, ResultData}

import scala.concurrent.{ExecutionContext, Future}

trait ClickService {
  def getData(configId: Int): Future[Option[ResultData]]  // i don't want return pure String here
}

class ClickServiceImpl(elasticsearchClient: ElasticsearchClient)
                      (implicit ec: ExecutionContext) extends ClickService {

  override def getData(configId: Int): Future[Option[ResultData]] = {
    val elasticSearchConf = ElasticSearchConf(configId, ElasticSearchFields(Set("item_id", "name")))

    val resStr = elasticsearchClient.viewData("click", "demo", elasticSearchConf) //TODO create conf for params
    resStr.map(v =>   Some(ResultData(v)))
  }
}
