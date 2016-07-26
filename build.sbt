name := "sparklify"

version := "0.0.1"

scalaVersion := "2.10.4"

// additional libraries
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.2" % "provided",
  "org.apache.spark" %% "spark-sql" % "1.6.2",
  "org.apache.spark" %% "spark-hive" % "1.6.2",
  "org.apache.parquet" % "parquet-hadoop" % "1.6.2",
  "org.apache.spark" % "spark-catalyst_2.10" % "1.6.2"
)
