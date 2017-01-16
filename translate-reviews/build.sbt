import io.gatling.sbt.GatlingPlugin

enablePlugins(GatlingPlugin)

name := "translate-reviews"

organization := "com.foodreviews"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps"
)

javaOptions in Gatling := overrideDefaultJavaOptions("-Xms1G", "-Xmx5G", "-Djsse.enableSNIExtension=false")

libraryDependencies ++= Seq( 
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.3" % "test",
  "io.gatling"            % "gatling-test-framework"    % "2.2.3" % "test"
)
