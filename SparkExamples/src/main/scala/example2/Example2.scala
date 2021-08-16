package example2

import org.apache.spark.sql.SparkSession

object Example2 extends App {


  val spark = SparkSession
    .builder
    .appName("Example1")
    .config("spark.master", "local")
    .enableHiveSupport() // Включаем поддержку Hive. Hive - это библиотека, позволяющая Spark-у работать с SQL
    .getOrCreate()

  val sampleSeq = Seq((1, "spark"), (2, "Big data"))

  val dataFrame = spark.createDataFrame(sampleSeq).toDF("id", "teknologi")
  dataFrame.show()

  dataFrame.write.format("csv").save("D://Example2") // сохранить данные из спрарка в папку по указанному пути. P.s. удалить её потом не забудь


  // Apache Hive – это SQL интерфейс доступа к данным для платформы Apache Hadoop.
  // Hive позволяет выполнять запросы, агрегировать и анализировать данные используя SQL синтаксис.
  // Для данных в файловой системе HDFS используется схема доступа на чтение, позволяющая обращаться с данными,
  // как с обыкновенной таблицей или реляционной СУБД.
  // Запросы HiveQL транслируются в Java-код заданий MapReduce.
  // https://www.bigdataschool.ru/wiki/hive

}
