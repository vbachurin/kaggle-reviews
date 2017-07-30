package com.foodreviews.spark

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.body.StringBody

import scala.concurrent.duration._
import scala.reflect.io.File

class TranslateReviews extends Simulation {

  val httpConf = http
    .baseURL("https://api.google.com") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/json,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val translationsFilePath = "../text_fr.csv"
  val headers = Map("Content-Type" -> "application/json")

  private val body = """{ "input_lang": "en",  "output_lang": "fr", "text": "${Text}" }"""
  val scn = scenario("Google Translate")
    .feed(
      csv("Reviews.csv")
//      .convert(splitToFitInBody)
     )
    .exec(http("en_to_fr")
      .post("/translate")
      .headers(headers)
      .body(StringBody(body)).asJSON
      .check(jsonPath("$.text").saveAs("translated"))
    )
    .exec { session =>
      val translated= session("translated").as[String]
      File(translationsFilePath).appendAll(s"$translated\n")
      session
    }

  before {
    println(s"Translated reviews will be written into: $translationsFilePath")
    File(translationsFilePath).delete()
  }

  setUp(scn.inject(constantUsersPerSec(100) during(2 hours)).protocols(httpConf))
    .assertions(global.failedRequests.percent.lte(100))
}
