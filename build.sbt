lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(publish := {})

lazy val commonSettings = Seq(
  version := "0.1",
  name := "advanced-scala",
  organization := "slowikps",
  scalaVersion := "2.13.1",
  credentials ++= Seq(
    Credentials(Path.userHome / ".sbt" / ".bintrayCredentials"),
    Credentials(Path.userHome / ".sbt" / ".nexusCredentials")
  ),
  resolvers ++= Seq(
    Resolver.bintrayRepo("vizog", "maven"),
    Resolver.jcenterRepo,
    Resolver.sonatypeRepo("releases"),
    "jitpack" at "https://jitpack.io/"
  ),
  libraryDependencies := dependencies,
)

val dependencies = Seq(
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.atnos"   %% "eff"       % "5.5.2",
  //Akka http
  "com.typesafe.akka" %% "akka-http"            % "10.1.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
  "com.typesafe.akka" %% "akka-stream"          % "2.6.1" // or whatever the latest version is
) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-generic-extras",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-optics"
).map(_           % "0.12.0") ++ Seq(
  "org.scalactic" %% "scalactic" % "3.1.0",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
