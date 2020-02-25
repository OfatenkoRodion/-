import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.ActionBasedSQLInterpolation
import slick.sql.SqlAction

import scala.concurrent.ExecutionContext

trait DinosaurDAO {

  def init: DBIO[Unit]

  def initIdGenerator: SqlAction[Int, NoStream, Effect]

  def selectALL: DBIO[Seq[Dinosaur]]

  def create(dinosaur: Dinosaur): DBIO[Dinosaur]

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

}
