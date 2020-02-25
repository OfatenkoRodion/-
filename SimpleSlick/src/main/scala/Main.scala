import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val db = Database.forURL(
    url = "jdbc:postgresql://localhost:5432/dino",
    user = "postgres",
    password = "1234",
    driver = "org.postgresql.Driver")

  val dinosaurModel = new DinosaurModel{}
  val dinosaurDAO: DinosaurDAO = new DinosaurDAOImpl(dinosaurModel)

/*  Await.result(db.run(dinosaurDAO.init), Duration.Inf)
  Await.result(db.run(dinosaurDAO.initIdGenerator), Duration.Inf)*/

  val dinosaur = Dinosaur(
    id = None,
    weight = 5678.7,
    height = 2.2,
    gender = true,
    isFly = false,
    isSwim = false,
    typeOfFood = TypeOfFood.Omnivores,
    lifespan = Some(15)
  )

  Await.result(db.run(dinosaurDAO.create(dinosaur)), Duration.Inf)
}
