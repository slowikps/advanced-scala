name := "advanced-scala"

version := "1.0"

scalaVersion := "2.12.4"

val circeVersion = "0.8.0"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.5")

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless"            % "2.3.3",
  "io.circe"    %% "circe-core"           % circeVersion,
  "io.circe"    %% "circe-generic"        % circeVersion,
  "io.circe"    %% "circe-parser"         % circeVersion,
  "io.circe"    %% "circe-generic-extras" % circeVersion,
  "io.circe"    %% "circe-optics"         % circeVersion,
  "org.atnos"   %% "eff"       % "4.5.0"
)

// to get types like Reader[String, ?] (with more than one type parameter) correctly inferred for scala 2.12.x
scalacOptions += "-Ypartial-unification"