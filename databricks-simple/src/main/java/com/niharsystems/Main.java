package com.niharsystems;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Create a SparkSession
        SparkSession spark = SparkSession.builder()
                .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
                .appName("Simple Spark Example")
                .master("local[*]")
                .getOrCreate();

        // Create a JavaSparkContext from SparkSession
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
/*
        // Create a sample RDD with some text
        JavaRDD<String> textRDD = sc.parallelize(Arrays.asList(
                "Hello Spark",
                "Hello Java",
                "Spark is awesome"
        ));

        // Split the text into words and count them
        JavaRDD<String> words = textRDD.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        System.out.println("Total words: " + words.count());

        // Print each word
        words.collect().forEach(System.out::println);
*/

        sc.hadoopConfiguration().set("fs.s3a.access.key", "2nZqqHPWEzu9JooKNoXO");
        sc.hadoopConfiguration().set("fs.s3a.secret.key", "DfFaWePTJsp5mB50pS2a7Iz00A6AgJEmdXWGyIOx");
        sc.hadoopConfiguration().set("fs.s3a.endpoint", "http://localhost:9000");
        sc.hadoopConfiguration().set("fs.s3a.path.style.access", "true");
        sc.hadoopConfiguration().set("fs.s3a.connection.ssl.enabled", "false");
        sc.hadoopConfiguration().set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        sc.hadoopConfiguration().set("fs.s3a.connection.ssl.enabled", "false");

        Dataset<Row> df = spark.read().option("multiline", "true").json("s3a://vj-bucket/sample.json");
        df.printSchema();
        df.select("user.address.zip").show();
//        System.out.println(df.count());
//        System.out.println(df.columns());
        spark.stop();
    }
}