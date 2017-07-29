name := "food-reviews"

version := "1.0"

scalaVersion := "2.11.8"
sparkVersion := "2.1.0"
sparkComponents += "sql"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.5",
  "io.gatling"            % "gatling-test-framework"    % "2.2.5",
  "org.rogach"            %% "scallop" % "3.0.3"
)