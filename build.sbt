name := "advanced-scala"

version := "1.0"

scalaVersion := "2.12.4"

val circeVersion = "0.9.1"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless"            % "2.3.3",
  "io.circe"    %% "circe-core"           % circeVersion,
  "io.circe"    %% "circe-generic"        % circeVersion,
  "io.circe"    %% "circe-parser"         % circeVersion,
  "io.circe"    %% "circe-generic-extras" % circeVersion,
  "io.circe"    %% "circe-optics"         % circeVersion,
  "org.atnos"   %% "eff"                  % "5.0.0",
  //Akka http
  "com.typesafe.akka" %% "akka-http"            % "10.1.0-RC2",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0-RC2",
  "com.typesafe.akka" %% "akka-stream"          % "2.5.8" // or whatever the latest version is
)


scalacOptions += "-Ypartial-unification"