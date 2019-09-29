lazy val akkaHttpVersion = "10.1.9"
lazy val akkaVersion = "2.5.25"
lazy val slickVersion = "3.3.2"

name := "akka-http-hello-world"

version := "0.1"

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)
