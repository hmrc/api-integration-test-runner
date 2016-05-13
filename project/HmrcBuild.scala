import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  val nameApp = "api-integration-test-runner"

  val scalatestVersion = "2.2.5"
  val cucumberVersion = "1.2.4"
  val junitVersion = "4.12"
  val sbtJunitInterfaceVersion = "0.11"
  val sprayJsonVersion = "1.3.2"
  val configVersion = "1.3.0"

  val appDependencies = Seq(
    "com.typesafe" % "config" % configVersion,
    "io.spray" %%  "spray-json" % sprayJsonVersion % "test",
    "org.scalatest" % "scalatest_2.11" % scalatestVersion % "test",
    "info.cukes" % "cucumber-scala_2.11" % cucumberVersion % "test",
    "info.cukes" % "cucumber-junit" % cucumberVersion % "test",
    "org.pegdown" % "pegdown" % "1.5.0" % "test",
    "com.novocode" % "junit-interface" % sbtJunitInterfaceVersion % "test"
  )

  lazy val addressModel = Project(nameApp, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      scalaVersion := "2.11.7",
      libraryDependencies ++= appDependencies,
      crossScalaVersions := Seq("2.11.7"),
      resolvers := Seq(
        Resolver.url("hmrc-releases",
          url("https://dl.bintray.com/hmrc/releases")),
        Resolver.bintrayRepo("hmrc", "releases"),
        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"
      )
    )
}

