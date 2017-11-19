name := "advanced-scala"

version := "1.0"

scalaVersion := "2.12.3"

val circeVersion = "0.8.0"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless"            % "2.3.2",
  "io.circe"    %% "circe-core"           % circeVersion,
  "io.circe"    %% "circe-generic"        % circeVersion,
  "io.circe"    %% "circe-parser"         % circeVersion,
  "io.circe"    %% "circe-generic-extras" % circeVersion,
  "io.circe"    %% "circe-optics"         % circeVersion
)
