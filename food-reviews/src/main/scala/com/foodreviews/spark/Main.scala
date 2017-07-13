package com.foodreviews.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by link on 12.07.2017.
  */
object Main {

  case class Review(
                     id: Int,
                     productId: Int,
                     userId: Int,
                     profileName: String,
                     helpfulnessNumerator: Int,
                     helpfulnessDenominator: Int,
                     score: Int,
                     time: Long,
                     summary: String,
                     text: String
                   )

  def main(args: Array[String]): Unit = {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
      .setMaster("local[*]") // The job will be running locally, use all cores
      .setAppName("FR1000MostUsedWords")
      .set("spark.sql.warehouse.dir", "C:/tmp") // Hack for Windows only

    // Creating Spark session
    val spark = SparkSession.builder.config(conf).getOrCreate()

    import spark.implicits._
    // Creating Spark data frame from file
    val ds = spark.sqlContext.read
      .format("com.databricks.spark.csv") // Use pre-defined CSV data format
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load("../amazon-fine-foods/Reviews.csv").as[Review]
    // The 'amazon-fine-foods' dir must be on the same level with 'food-reviews' dir



//    FR1000MostActiveUsers.run()
//    FR1000MostCommentedFood.run()
    FR1000MostUsedWords.run(spark, ds)


    // Closing Spark session
    spark.stop()
  }

}
