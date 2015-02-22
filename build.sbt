organization := "com.itrative"

name := "spray-slick-rest"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= {
  Seq(
    "io.spray" %% "spray-can" % "1.3.2",
    "io.spray" %% "spray-routing" % "1.3.2",
    "io.spray" %% "spray-json" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.3.9",
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "mysql" % "mysql-connector-java" % "5.1.34"
  )
}