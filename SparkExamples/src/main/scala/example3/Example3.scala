package example3

import org.apache.spark.sql.SparkSession

import java.util.Properties

object Example3 extends App {
  // Для этого примера нужно установить postgresql
  // https://www.postgresql.org/download/windows/

  val spark = SparkSession
    .builder
    .appName("Example1")
    .config("spark.master", "local")
    .enableHiveSupport() // Включаем поддержку Hive. Hive - это библиотека, позволяющая Spark-у работать с SQL
    .getOrCreate()

  val pgConnectingProperties = new Properties()
  pgConnectingProperties.put("user", "postgres") // ваш логин в ПГ
  pgConnectingProperties.put("password", "1")// ваш пароль в ПГ


  val pgTable = "public.teknologis" // Надо создать ручками в бд
  //  CREATE TABLE teknologis(
  //    id INTEGER,
  //    teknologi VARCHAR
  //  )

  val pgDataBase = "teknologis_database" // надо создать БД ручками в ПГ

  val pgCourseDataFrame = spark.read.jdbc(s"jdbc:postgresql://localhost:5432/$pgDataBase", pgTable, pgConnectingProperties)// Подключаемся к базе


  pgCourseDataFrame.show() // Чтобы что-то показало, надо вставить в БД записи
}
