name := "Group Project"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.3.2",
	"org.slf4j" % "slf4j-api" % "1.7.25",
	"ch.qos.logback" % "logback-classic" % "1.2.3",
	"org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
	"org.cloudsimplus" % "cloudsim-plus" % "4.3.1"
)


// set the main class for 'sbt run'
//mainClass in (Compile, run) := Some("cloudsimplus.examples.DynamicVmAllocationPolicyBestFitExample")