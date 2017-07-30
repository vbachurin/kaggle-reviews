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

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"
