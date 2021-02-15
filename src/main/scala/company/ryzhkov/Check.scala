package company.ryzhkov

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Check extends App {
  val sparkConf = new SparkConf().setAppName("simple").setMaster("local[1]")

  val sparkSession = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config(sparkConf)
    .getOrCreate()

  val sc = sparkSession.sparkContext

  val parquetFileDF = sparkSession
    .read
    .parquet("foo/date=2021-02-02/part-00000-95f09db0-546c-42b8-8e51-f13199c2f3a8.c000.snappy.parquet")

  parquetFileDF.show()
}
