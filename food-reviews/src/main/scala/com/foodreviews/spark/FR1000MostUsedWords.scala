package com.foodreviews.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.{desc, concat, lit, col}
import org.apache.spark.sql.{SparkSession}
import org.apache.spark.{SparkConf}

/**
  * Created by vladyslav.bachurin on 1/11/2017.
  */
object FR1000MostUsedWords {

  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
      .setMaster("local[*]") // The job will be running locally, use all cores
      .setAppName("FR1000MostUsedWords")
      .set("spark.sql.warehouse.dir", "C:/tmp") // Hack for Windows only

    // Creating Spark session
    val spark = SparkSession.builder.config(conf).getOrCreate()

    // Creating Spark data frame from file
    val df = spark.sqlContext.read
      .format("com.databricks.spark.csv") // Use pre-defined CSV data format
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load("../amazon-fine-foods/Reviews.csv")
    // The 'amazon-fine-foods' dir must be on the same level with 'food-reviews' dir

    // Will be counting words for Summary and Text together, that is why use concat
    val summaryAndText = df.select(concat(col("Summary"), lit(" "), col("Text")))

    // Using the implicit encoder
    import spark.implicits._

    // Splitting text into words (by anything but words and apostrophes)
    val words = summaryAndText.flatMap(x => x.toString().toLowerCase().split("[^\\w']+"))

    // Grouping by words, counting instances in each group, ordering by count
    val counts = words.groupBy("value").count().orderBy(desc("count"));

    // Taking 1000 most used words
    counts.take(1000).foreach(println)

    // Closing Spark session
    spark.stop()
  }
}
