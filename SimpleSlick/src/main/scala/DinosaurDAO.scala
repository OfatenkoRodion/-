import java.time.Instant

import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

import scala.concurrent.ExecutionContext

trait DinosaurDAO {

  def init: DBIO[Unit]

  protected def initIdGenerator: SqlAction[Int, NoStream, Effect]

  def selectALL: DBIO[Seq[Dinosaur]]

  def create(dinosaur: Dinosaur): DBIO[Dinosaur]

  def update(dinosaur: Dinosaur): DBIO[Dinosaur]

  def take(n: Int): DBIO[Seq[Dinosaur]]

  def mapDinosaurParam: DBIO[Seq[BodyParams]]

  def leftJoinExample: DBIO[Seq[(Dinosaur, Option[EventLogs])]]

  def rightJoinExample: DBIO[Seq[(Option[Dinosaur], EventLogs)]]

  def fullJoinExample: DBIO[Seq[(Option[Dinosaur], Option[EventLogs])]]

  def innerJoinExample: DBIO[Seq[(Dinosaur, EventLogs)]]

  def crossJoinExample: DBIO[Seq[(Dinosaur, EventLogs)]]
}

class DinosaurDAOImpl(dinosaurModel: DinosaurModel with EventLogsModel)
                     (implicit executionContext: ExecutionContext) extends DinosaurDAO {

  import dinosaurModel.{dinosaurs, dinosaursSeq, eventLogs}

  // Склеиваем все что нужно для создания, не отдельно же вызывать?)
  def init: DBIO[Unit] = {
    for {
      _ <- (dinosaurs.schema ++ eventLogs.schema).create
      _ <- initIdGenerator
    } yield {}
  }

  protected def initIdGenerator: SqlAction[Int, NoStream, Effect] = sqlu"""create sequence "dino_id_seq" start with 1 increment by 1;"""

  def selectALL: DBIO[Seq[Dinosaur]] = dinosaurs.result


  def create(dinosaur: Dinosaur): DBIO[Dinosaur] = {
    for {
      newId <- dinosaursSeq.next.result
      dino = dinosaur.copy(id = Some(newId))
      _ <- dinosaurs += dino
      _ <- eventLogs += EventLogs(None, newId, Some(Instant.now), None)
    } yield dino
  }

  def update(dinosaur: Dinosaur): DBIO[Dinosaur] = {
    for {
      _ <- dinosaurs
        .update(dinosaur)
      _ <- dinosaur.id.map(idReal => eventLogs += EventLogs(None, idReal, None, Some(Instant.now))).getOrElse(DBIO.successful(()))
    } yield dinosaur
  }

  //<ерет первые n элементов. Удобная штука, когда надо обрабатывать что-то по очереди, например некие заявки
  def take(n: Int): DBIO[Seq[Dinosaur]] = {
    dinosaurs
      .take(n)
      .result
  }

  //Конвертируем результат в новый кейс класс. Фактически select лишь нескольких полей.
  // Полезная штука, когда у нас в таблице дофига полей, а значения нужны лишь из нескольких
  def mapDinosaurParam: DBIO[Seq[BodyParams]] = {
    dinosaurs
      .map(d => (d.weight, d.height))
      .result
      //.map(_.map(v => BodyParams(v._1, v._2))) // or
      .map(_.map((BodyParams.apply _).tupled))
  }

  def leftJoinExample: DBIO[Seq[(Dinosaur, Option[EventLogs])]] = {
    for {
      (d, e) <- dinosaurs joinLeft eventLogs on (_.id === _.dinosaurId)
    } yield (d, e)
  }.result

  def rightJoinExample: DBIO[Seq[(Option[Dinosaur], EventLogs)]] = {
    for {
      (d, e) <- dinosaurs joinRight eventLogs on (_.id === _.dinosaurId)
    } yield (d, e)
  }.result

  def fullJoinExample: DBIO[Seq[(Option[Dinosaur], Option[EventLogs])]] = {
    for {
      (d, e) <- dinosaurs joinFull eventLogs on (_.id === _.dinosaurId)
    } yield (d, e)
  }.result

  def innerJoinExample: DBIO[Seq[(Dinosaur, EventLogs)]] = {
    for {
      (d, e) <- dinosaurs join eventLogs on (_.id === _.dinosaurId)
    } yield (d, e)
  }.result

  // Fuuuuuuuuuuuu
  def crossJoinExample: DBIO[Seq[(Dinosaur, EventLogs)]] = {
    for {
      (d, e) <- dinosaurs join eventLogs
    } yield (d, e)
  }.result

}