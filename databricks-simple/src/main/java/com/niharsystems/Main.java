package com.niharsystems;

import io.delta.sql.DeltaSparkSessionExtension;
import org.apache.spark.sql.*;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.IntegerType;
import org.apache.spark.sql.types.StringType;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String deltaTablePath = "vj-bucket";
        // Create a SparkSession
        SparkSession spark = SparkSession.builder()
                .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
//                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .config("spark.jars.packages", "org.apache.hadoop:hadoop-azure:3.3.4,io.delta:delta-core_2.12:3.3.0,io.delta:delta-sharing-spark_2.12:3.3.0")
                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
                .config("spark.sql.extensions", DeltaSparkSessionExtension.class.getName())
                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
//                .config("spark.jars.packages", "io.delta:delta-spark_2.12:3.3.0")
//                .config("spark.jars.packages", "io.delta:delta-sharing-spark_2.12:3.3.0")
                .config("spark.delta.logStore.class", "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore")
                .config("spark.hadoop.fs.s3a.endpoint", "http://localhost:9000")
                .config("spark.hadoop.fs.s3a.access.key", "2nZqqHPWEzu9JooKNoXO")
                .config("spark.hadoop.fs.s3a.secret.key", "DfFaWePTJsp5mB50pS2a7Iz00A6AgJEmdXWGyIOx")
                .config("spark.hadoop.fs.s3a.path.style.access", "true")
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
                .config("spark.sql.warehouse.dir", "s3a://" + deltaTablePath + "/spark-warehouse")
                .appName("Simple Spark Example")
                .master("local[*]")
                .getOrCreate();

//        config("spark.jars.packages", "io.delta:delta-core_2.12:2.0.0")
//                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
//                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
//                .config("spark.delta.logStore.class", "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore")
//        // Create a JavaSparkContext from SparkSession
//        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
////        sc.hadoopConfiguration().set("fs.s3a.access.key", "2nZqqHPWEzu9JooKNoXO");
////        sc.hadoopConfiguration().set("fs.s3a.secret.key", "DfFaWePTJsp5mB50pS2a7Iz00A6AgJEmdXWGyIOx");
////        sc.hadoopConfiguration().set("fs.s3a.endpoint", "http://localhost:9000");
//        sc.hadoopConfiguration().set("fs.s3a.path.style.access", "true");
//        sc.hadoopConfiguration().set("fs.s3a.connection.ssl.enabled", "false");
//        sc.hadoopConfiguration().set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
//        sc.hadoopConfiguration().set("fs.s3a.connection.ssl.enabled", "false");
        List<Row> data = Arrays.asList(
                RowFactory.create(30),
                RowFactory.create(23),
                RowFactory.create(56)
        );
//        List<Row> data = Arrays.asList(
//                RowFactory.create("John Doe", 30, "Engineering"),
//                RowFactory.create("Jane Smith", 28, "Marketing"),
//                RowFactory.create("Bob Johnson", 35, "Sales")
//        );

        // Define the schema for our data
        StructType schema = new StructType()
                .add("age", DataTypes.IntegerType, false);
//        StructType schema = new StructType()
//                .add("name", new StringType(), false)
//                .add("age", new IntegerType(), false)
//                .add("department", new StringType(), false);

        Dataset<Row> df1 = spark.read().option("multiline", "true").json("s3a://vj-bucket/sample.json");
        df1.printSchema();
        df1.select("user.address.zip").show();

        Dataset<Row> df = spark.createDataFrame(data, schema);
        df.write()
//                .format("delta")
//                .option("delta.compatibility.symlinkFormatManifest.enabled", "true")
                .mode("overwrite")  // Can be "append" for adding new data
                .save("s3a://" + deltaTablePath + "/test");

//        Dataset<Long> data = spark.range(0, 5);
//        data.write().format("delta").save("s3a://vj-bucket/delta-table");


//        System.out.println(df.count());
//        System.out.println(df.columns());
        spark.stop();
    }
}