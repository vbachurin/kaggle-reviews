package com.foodreviews

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.{concat, desc, lit}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.rogach.scallop._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val path = opt[String](default = Option("."))
  val file = opt[String](default = Option("Reviews.csv"))
  val translate = opt[Boolean](default = Option(false))
  val tasks = trailArg[List[String]](required = false, default = Option(Nil))
  verify()
}

object Main {

  Logger.getLogger("org").setLevel(Level.ERROR)

  val spark =
    SparkSession
      .builder()
      .appName("Food Reviews")
      .master("local[4]") // local[4] to use 4 cores
      .config("spark.executor.memory", "500m")
      .getOrCreate()

  // Set the log level to only print errors
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  def main(args: Array[String]): Unit = {

    val conf = new Conf(args)

    // Creating Spark data frame from file
    val df = spark.sqlContext.read
      .format("com.databricks.spark.csv") // Use pre-defined CSV data format
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(s"${conf.path()}/${conf.file()}")
//      .distinct() // Drop duplicates - awfully time-consuming

    runTasks(conf.tasks())

    def runTasks(tasks: List[String]): Unit = {
      tasks match {
        case Nil => println("No more Spark tasks to run.")
        case "mostActiveUsers"    :: t => mostActiveUsers(df); runTasks(t)
        case "mostCommentedFood"  :: t => mostCommentedFood(df); runTasks(t)
        case "mostUsedWords"      :: t => mostUsedWords(df); runTasks(t)
        case List("all") => mostActiveUsers(df); mostCommentedFood(df); mostUsedWords(df)
        case _ => println("One of the task names is incorrect or none specified. " +
          "Possible values: all mostActiveUsers mostCommentedFood mostUsedWords")
      }
    }

    // Closing Spark session
    spark.stop()

    if (conf.translate()) translate(conf.path())
  }

  def mostActiveUsers(df: DataFrame) = {
    df.select($"ProfileName").groupBy($"ProfileName").count().orderBy(desc("count")).limit(1000).orderBy("ProfileName").show(1000)
  }

  def mostCommentedFood(df: DataFrame) =
    df.select($"ProductId").groupBy($"ProductId").count().orderBy(desc("count")).limit(1000).orderBy("ProductId").show(1000)

  def mostUsedWords(df: DataFrame) = {
    // Will be counting words for Summary and Text together, that is why use concat
    val summaryAndText = df.select(concat($"Summary", lit(" "), $"Text"))

    // Splitting text into words (by anything but words and apostrophes)
    val words = summaryAndText.flatMap(_.toString().toLowerCase().split("[^\\w']+").filter(_ != ""))

    // Grouping by words, counting instances in each group, ordering by count
    words.groupBy("value").count().orderBy(desc("count")).limit(1000).orderBy("value").show(1000)
  }

  def translate(path: String) = {
    // This sets the class for the Simulation we want to run.
    val simClass = classOf[TranslateReviews].getName

    val props = new GatlingPropertiesBuilder
    props.sourcesDirectory("./src/main/scala")
    props.binariesDirectory("./target/scala-2.11/classes")
    props.dataDirectory(path)
    props.simulationClass(simClass)
    println("Sending translation requests ...")
    Gatling.fromMap(props.build)
  }
}