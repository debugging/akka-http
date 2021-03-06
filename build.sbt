lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion    = "2.6.10"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.3"
    )),
    name := "akka-http",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "org.typelevel" %% "cats-core" % "2.1.1",
      "com.lihaoyi" %% "scalatags" % "0.8.2",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,

      

      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  ).enablePlugins(JavaAppPackaging)


addCommandAlias("c", "compile")
addCommandAlias("cc", "~compile")
addCommandAlias("r", "reload")  