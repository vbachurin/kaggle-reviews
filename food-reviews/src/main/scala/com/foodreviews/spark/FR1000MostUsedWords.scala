package com.foodreviews.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
  * Created by vladyslav.bachurin on 1/11/2017.
  */
object FR1000MostUsedWords {
  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

/*
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("FR1000MostUsedWords")

    val spark = SparkSession.builder.config(conf).getOrCreate()
    val sc = spark.read.option("header","true").csv("../amazon-fine-foods/Reviews.csv")
*/

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "FR1000MostUsedWords")

    // Load up each line of the reviews data into an RDD
    val lines = sc.textFile("../amazon-fine-foods/Reviews.csv")


    val header = lines.first() // extract header
    val data = lines.filter(row => row != header) // filter out header

    // Convert each line to a string, split it out by tabs, and extract the fourth field.
    //val summaryAndText = lines.map(x => {x.toString().split(","); (x(8).toString, x(9).toString)})
    val summaryAndText = data.map(x => {val y = x.toString().split(","); (y(8), y(9))})


    val words = summaryAndText.flatMap(x => ("" + x._1 + " " + x._2 + " "))

    words.take(100).foreach(print)
/*
    // Flip
    val flipped = results.map(x => (x._2, x._1))

    // Sort the resulting map of (word, count) tuples
    val sortedResults = flipped.sortByKey(false)

    val first1000 = sortedResults.take(1000)

    // Print each result on its own line.
    first1000.foreach(println)
    */
  }
}
