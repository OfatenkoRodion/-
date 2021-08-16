package example1

import org.apache.spark.sql.SparkSession

object Example1 extends App {

  // Создаем и запускаем систему Spark

  val spark = SparkSession
    .builder
    .appName("Example1")
    .config("spark.master", "local") // Конфиги по умолчанию для запуска на локальном серваке
    .getOrCreate()

  val sampleSeq: Seq[Tuple2[Int, String]] = Seq((1, "spark"), (2, "Big data")) // Данные, которые сохраняем.  Смотрите, цифра - это как буд-то id, а строка как значение.

  val dataFrame = spark.createDataFrame(sampleSeq).toDF("id", "teknologi") // Говорим превратить наши исходные данные в данные spark-a.
  // Заметьте, что данные не свазанны с названиями полей, и мы можем дать любые названия.
  // Или даже вызвать это два раза и дать разные названия

  dataFrame.show() // Вместо println


}
