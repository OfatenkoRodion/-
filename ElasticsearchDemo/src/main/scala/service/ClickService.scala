package service

import client.{ElasticsearchClient, RedisClientDemoImpl}
import model.{ElasticSearchBuildRequest, ElasticSearchConf, ResultData}

import scala.concurrent.{ExecutionContext, Future}

trait ClickService {
  def getData(configId: Int, from: Int, size: Int): Future[Option[ResultData]] // i don't want return pure String here
}

class ClickServiceImpl(elasticsearchClient: ElasticsearchClient,
                       redisClientDemoImpl: RedisClientDemoImpl)
                      (implicit ec: ExecutionContext) extends ClickService {

  override def getData(configId: Int, from: Int, size: Int): Future[Option[ResultData]] = {
    for {
      elasticSearchConfOption <- redisClientDemoImpl.getObj[ElasticSearchConf](configId.toString)
      resultData <- elasticSearchConfOption.fold(Future.successful[Option[ResultData]](None))(v => getResultData(from, size, v))  // TODO need some monad magic
    } yield resultData
  }

  private def getResultData(from: Int, size: Int, conf: ElasticSearchConf): Future[Option[ResultData]] = {

    for {
      resStrData <- elasticsearchClient.viewData("click", "demo", ElasticSearchBuildRequest(from, size, conf.elasticSearchFields._source)) // TODO move names to conf
    } yield Some(ResultData(resStrData))
  }
}
