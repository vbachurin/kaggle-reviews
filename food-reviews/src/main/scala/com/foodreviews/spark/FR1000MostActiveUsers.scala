package com.foodreviews.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

/**
  * Created by vladyslav.bachurin on 1/11/2017.
  */
object FR1000MostActiveUsers {
  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "FR1000MostActiveUsers")

    // Load up each line of the usernames data into an RDD
    val lines = sc.textFile("../amazon-fine-foods/Reviews.csv")

    // Convert each line to a string, split it out by tabs, and extract the fourth field.
    val usernames = lines.map(x => (x.split(",")(3), 1))

    // Count up how many times each value (user name) occurs
    val results = usernames.reduceByKey(_ + _)

    // Flip
    val flipped = results.map(x => (x._2, x._1))

    // Sort the resulting map of (username, count) tuples
    val sortedResults = flipped.sortByKey(false)

    // Take first 1000
    val first1000 = sortedResults.take(1000)

    // Print each result on its own line.
    first1000.foreach(println)
  }
}
