import _root_.scalafix.sbt.BuildInfo.{ scala212, scalafixVersion }

val semanticdbV = "4.3.17"
val scala213 = "2.13.2"

inThisBuild(List(
  crossScalaVersions := List(scala213, "2.12.11"),
  libraryDependencies += "org.scalameta" %% "semanticdb-scalac-core" % semanticdbV cross CrossVersion.full,
  organization := "org.scala-lang",
  licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(Developer("", "", "", url("https://github.com/scala/scala-rewrites/graphs/contributors"))),
  homepage := Some(url("https://github.com/scala/scala-rewrites")),
  scalaVersion := scala213,
))

skip in publish := true

val rewrites = project.settings(
  moduleName := "scala-rewrites",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-rules" % scalafixVersion,
)

val input = project.settings(
  addCompilerPlugin("org.scalameta" %% "semanticdb-scalac" % semanticdbV cross CrossVersion.full),
  scalacOptions ++= List("-Yrangepos", "-P:semanticdb:synthetics:on"),
  skip in publish := true,
)

val output = project.settings(skip in publish := true)

val output213 = output.withId("output213").settings(
  target := (target.value / "../target-2.13").getCanonicalFile,
  scalaVersion := "2.13.0",
)

val tests = project.dependsOn(rewrites).enablePlugins(ScalafixTestkitPlugin).settings(
  skip in publish := true,
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % scalafixVersion % Test cross CrossVersion.full,
  compile in Compile := (compile in Compile).dependsOn(compile in (input, Compile)).value,
  scalafixTestkitOutputSourceDirectories := (sourceDirectories in (output, Compile)).value,
  scalafixTestkitInputSourceDirectories := (sourceDirectories in (input, Compile)).value,
  scalafixTestkitInputClasspath := (fullClasspath in (input, Compile)).value,
)
