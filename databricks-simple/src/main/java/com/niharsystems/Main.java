package com.niharsystems;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Create a SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("Simple Spark Example")
                .master("local[*]")
                .getOrCreate();

        // Create a JavaSparkContext from SparkSession
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

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

        // Stop the SparkSession
        spark.stop();
    }
}