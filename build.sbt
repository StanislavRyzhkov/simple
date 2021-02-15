name := "simple"

version := "0.1"

scalaVersion := "2.12.12"

val sparkV = "3.0.1"

lazy val common = (project in file("."))
  .enablePlugins(AssemblyPlugin)
  .settings(
    name := "simple",
    libraryDependencies ++= Seq(
      "io.spray" %% "spray-json" % "1.3.6",
      "org.apache.spark" %% "spark-core" % sparkV,
      "org.apache.spark" %% "spark-sql" % sparkV,
      "org.scalatest" %% "scalatest" % "3.2.0" % Test,
      "org.scalatest" %% "scalatest-core" % "3.2.0",
      "org.scalatest" %% "scalatest-funsuite" % "3.2.0" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:postfixOps"
    ),
    mainClass in (Compile, run) := Some("company.ryzhkov.Program"),
    mainClass in (assembly) := Some("company.ryzhkov.Program")
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}
