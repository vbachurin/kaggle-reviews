package com.foodreviews.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

/**
  * Created by vladyslav.bachurin on 1/11/2017.
  */
object FR1000MostCommentedFood {
  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "FR1000MostCommentedFood")

    // Load up each line of the foodId data into an RDD
    val lines = sc.textFile("../amazon-fine-foods/Reviews.csv")

    // Convert each line to a string, split it out by tabs, and extract the fourth field.
    val foodId = lines.map(x => (x.split(",")(1), 1))

    // Count up how many times each value (foodId) occurs
    val results = foodId.reduceByKey(_ + _)

    // Flip
    val flipped = results.map(x => (x._2, x._1))

    // Sort the resulting map of (foodId, count) tuples
    val sortedResults = flipped.sortByKey(false)

    // Take first 1000
    val first1000 = sortedResults.take(1000)

    // Print each result on its own line.
    first1000.foreach(println)
  }
}
