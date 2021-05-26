package service

import client.{ElasticsearchClient, RedisClientDemoImpl}
import model.{ElasticSearchConf, ResultData}
import scala.concurrent.{ExecutionContext, Future}

trait ClickService {
  def getData(configId: Int): Future[Option[ResultData]] // i don't want return pure String here
}

class ClickServiceImpl(elasticsearchClient: ElasticsearchClient,
                       redisClientDemoImpl: RedisClientDemoImpl)
                      (implicit ec: ExecutionContext) extends ClickService {

  override def getData(configId: Int): Future[Option[ResultData]] = {
    for {
      elasticSearchConfOption <- redisClientDemoImpl.getObj[ElasticSearchConf](configId.toString)
      resultData <- elasticSearchConfOption.fold(Future.successful[Option[ResultData]](None))(v => getResultData(v))  // TODO need some monad magic
    } yield resultData
  }

  private def getResultData(conf: ElasticSearchConf): Future[Option[ResultData]] = {
    for {
      resStrData <- elasticsearchClient.viewData("click", "demo", conf) // TODO move names to conf
    } yield Some(ResultData(resStrData))
  }
}
