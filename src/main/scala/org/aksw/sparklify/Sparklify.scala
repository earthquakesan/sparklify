// scalastyle:off println
package org.aksw.sparklify

import org.apache.spark.SparkContext._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext

object Sparklify {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("Sparklify")
    val ctx = new SparkContext(sparkConf)

    val hiveCtx = new HiveContext(ctx)

    val filePath = "file://".concat(args[0])
    println("Parsing " + filePath)
    val input = hiveCtx.jsonFile(filePath)
    input.registerTempTable("tweets")
    val topTweets = hiveCtx.sql("SELECT text, retweetCount FROM tweets ORDER BY retweetCount LIMIT 10")
    val topTweetText = topTweets.map(row => row.getString(0))
    println(topTweetText.collect().mkString(","))

    ctx.stop()
  }
}
// scalastyle:on println
