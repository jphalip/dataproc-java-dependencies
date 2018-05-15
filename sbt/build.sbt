lazy val commonSettings = Seq(
  organization := "dataproc-java-dependencies-demo",
  name := "translate-example",
  version := "1.0",
  scalaVersion := "2.11.8",
)
lazy val shaded = (project in file("."))
  .settings(commonSettings)

mainClass in (Compile, packageBin) := Some("demo.TranslateExample")



/*libraryDependencies ++= Seq(
 "com.google.cloud" % "google-cloud-translate" % "1.29.0",
 "org.apache.spark" % "spark-sql_2.11" % "2.2.1"
   exclude("commons-beanutils", "commons-beanutils-core")
   exclude("commons-collections", "commons-collections")
   exclude("commons-logging", "commons-logging")
   //exclude("org.apache.hadoop", "hadoop-yarn-common")
   exclude("org.spark-project.spark", "unused")
   exclude("org.apache.spark", "spark-network-common_2.11")
   exclude("org.apache.spark", "spark-network-shuffle_2.11")
   exclude("org.slf4j", "jcl-over-slf4j")
   exclude("org.codehaus.janino", "janino")
)*/

val excludedOrgs = Seq(
  "commons-beanutils",
  "commons-collections",
  "commons-logging",
  "org.apache.hadoop",
  "org.spark-project.spark",
  "org.slf4j",
  "org.codehaus.janino"
)

libraryDependencies ++= Seq(
  "com.google.cloud" % "google-cloud-translate" % "1.29.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.2.1")
  .map(_.excludeAll(excludedOrgs.map(ExclusionRule(_)): _*))

assemblyMergeStrategy in assembly := {
  case PathList("org", "aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.fasterxml.**" -> "repackaged.com.fasterxml.@1").inAll,
  ShadeRule.rename("com.google.api.**" -> "repackaged.com.google.api.@1").inAll,
  ShadeRule.rename("com.google.cloud.hadoop.gcsio.**" -> "repackaged.com.google.cloud.hadoop.gcsio.@1").inAll,
  ShadeRule.rename("com.google.cloud.hadoop.util.**" -> "repackaged.com.google.cloud.hadoop.util.@1").inAll,
  ShadeRule.rename("com.google.common.**" -> "repackaged.com.google.common.@1").inAll,
  ShadeRule.rename("org.apache.commons.codec.**" -> "repackaged.org.apache.commons.codec.@1").inAll,
  ShadeRule.rename("org.apache.http.**" -> "repackaged.org.apache.http.@1").inAll
)