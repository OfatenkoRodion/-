import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

import scala.concurrent.ExecutionContext

trait DinosaurDAO {

  def init: DBIO[Unit]

  def initIdGenerator: SqlAction[Int, NoStream, Effect]

  def selectALL: DBIO[Seq[Dinosaur]]

  def create(dinosaur: Dinosaur): DBIO[Dinosaur]

  def take(n: Int): DBIO[Seq[Dinosaur]]

  def mapDinosaurParam:  DBIO[Seq[BodyParams]]
}

class DinosaurDAOImpl(dinosaurModel: DinosaurModel)
                     (implicit executionContext: ExecutionContext) extends DinosaurDAO {

  import dinosaurModel.{dinosaurs, dinosaursSeq}

  def init: DBIO[Unit] = dinosaurModel.init

  def initIdGenerator: SqlAction[Int, NoStream, Effect] = sqlu"""create sequence "dino_id_seq" start with 1 increment by 1;"""

  def selectALL: DBIO[Seq[Dinosaur]] = dinosaurs.result

  def create(dinosaur: Dinosaur): DBIO[Dinosaur] = {
    for {
      newId <- dinosaursSeq.next.result
      dino = dinosaur.copy(id = Some(newId))
      _ <- dinosaurs += dino
    } yield dino
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

}