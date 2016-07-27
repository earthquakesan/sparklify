// scalastyle:off println
package org.aksw.sparklify

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StructType, StringType, StructField}
import org.apache.spark.sql.DataFrame
import org.dissect.rdf.spark.utils.NTripleReader.NTripleReader

import java.io.File

object Sparklify {

  def main(args: Array[String]) {
    val spark = SparkSession.builder
      .master("local")
      .appName("Sparklify")
      .enableHiveSupport()
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryo.serializer", "org.dissect.rdf.spark.io.JenaKryoRegistrator")
      .getOrCreate()

    val filePath = args(0)
    println("Parsing " + filePath)
    val file = new File(filePath)

    val triples = new NTripleReader(file).toSeq
    val graph = spark.sparkContext.parallelize(triples)

    val schemaString = "subject predicate object"
    val fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)
    val graphRow = graph.map {
      case (subject, predicate, objectURI, "") => Row(subject, predicate, objectURI)
      case (subject, predicate, "", objectLiteral) => Row(subject, predicate, objectLiteral)
    }

    val graphDF = spark.createDataFrame(graphRow, schema)
    graphDF.write.mode("overwrite").saveAsTable("triples")

    //results.take(10).foreach(println)
    //println(graph.collect())
    //graph.foreach(println)

    spark.stop()
  }
}
// scalastyle:on println
