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

    val spoTableSchemaString = "subject predicate object"
    val spoTableFields = spoTableSchemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val spoTableSchema = StructType(spoTableFields)

    val spoTableRow = graph.filter(
      tuple => tuple._4 == ""
    ).map {
      case (subject, predicate, objectURI, literal) => Row(subject, predicate, objectURI)
    }

    val spoTableDF = spark.createDataFrame(spoTableRow, spoTableSchema)
    spoTableDF.write.mode("overwrite").saveAsTable("spo")


    //val spltTableSchemaString = "subject predicate literal type"
    val spltTableSchemaString = "subject predicate literal"
    val spltTableFields = spltTableSchemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val spltTableSchema = StructType(spltTableFields)

    //TODO: should be subject predicate literal type, for now keep type together with literal
    val spltTableRow = graph.filter(
      tuple => tuple._3 == ""
    ).map {
      case (subject, predicate, objectURI, objectLiteral) => (subject, predicate, objectLiteral)
    }.map(
      tuple => Row(tuple._1, tuple._2, tuple._3.split('"')(1))
    )

    //Create dataframes and save to DB
    val spltTableDF = spark.createDataFrame(spltTableRow, spltTableSchema)
    spltTableDF.write.mode("overwrite").saveAsTable("splt")

    //results.take(10).foreach(println)
    //println(graph.collect())
    //graph.foreach(println)

    spark.stop()
  }
}
// scalastyle:on println
