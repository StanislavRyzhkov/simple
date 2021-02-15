package company.ryzhkov

import company.ryzhkov.AppJsonProtocol._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import spray.json._

import java.io.File
import scala.util.Try

object FooBarJob extends App {

  def extractNumber(s: String): Option[Int] =
    Try {
      s.split("data-raw-bss.type.", 2)
        .last
        .split("-", 2)
        .iterator
        .next()
        .toInt
    }.toOption

  val sparkConf = new SparkConf().setAppName("simple").setMaster("local[1]")

  val sparkSession = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config(sparkConf)
    .getOrCreate()

  val sc = sparkSession.sparkContext

  val res = new File("dir")
    .listFiles()
    .toSeq
    .map(_.toString)
    .groupBy { extractNumber(_).getOrElse(-1) }
    .filter(_._1 != -1)

  import sparkSession.implicits._

  def createRDD(fileSeq: Seq[String]): RDD[String] =
    sc.textFile(fileSeq.mkString(",")).filter(_.nonEmpty)

  def writeToParquet(df: DataFrame, parquetDirName: String): Unit =
    df.write.partitionBy("date").parquet(parquetDirName)

  res.foreach {
    case (1, fileSeq) =>
      val df = createRDD(fileSeq).map(_.parseJson.convertTo[Foo]).toDF()
      writeToParquet(df, "foo")
    case (2, fileSeq) =>
      val df = createRDD(fileSeq).map(_.parseJson.convertTo[Bar]).toDF()
      writeToParquet(df, "bar")
    case _ =>
  }
}
