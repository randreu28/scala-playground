val scala3Version = "3.4.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-playground",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "dev.zio" %% "zio" % "2.1.5",
      "dev.zio" %% "zio-streams" % "2.1.5",
      "dev.zio" %% "zio-http" % "3.0.0-RC9",
      "io.getquill" %% "quill-jdbc-zio" % "4.8.4",
      "org.xerial" % "sqlite-jdbc" % "3.28.0"
    )
  )
