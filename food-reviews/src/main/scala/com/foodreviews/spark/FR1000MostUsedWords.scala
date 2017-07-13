package com.foodreviews.spark

import com.foodreviews.spark.Main.Review
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{col, concat, desc, lit}

/**
  * Created by vladyslav.bachurin on 1/11/2017.
  */
object FR1000MostUsedWords {
  def run(spark: SparkSession , df: Dataset[Review]): Unit = {

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

  }
}
