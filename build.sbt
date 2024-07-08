val scala3Version = "3.4.2"
val circeVersion = "0.14.1"
val http4sVersion = "0.23.18"
val log4catsVersion = "2.7.0"

lazy val root = project
  .in(file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-playground",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    Compile / run / fork := true,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC4",
      "org.xerial" % "sqlite-jdbc" % "3.36.0.3",
      "org.mindrot" % "jbcrypt" % "0.4",
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.11"
    )
  )
