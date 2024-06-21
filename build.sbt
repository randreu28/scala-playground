val scala3Version = "3.4.2"

lazy val root = project
  .in(file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-playground",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.4",
      "com.typesafe.akka" %% "akka-http" % "10.5.0",
      "com.typesafe.akka" %% "akka-stream" % "2.8.4",
      "ch.qos.logback" % "logback-classic" % "1.2.11",
      "org.slf4j" % "slf4j-api" % "1.7.36"
    )
  )
