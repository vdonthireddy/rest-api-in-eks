# run the following docker container for min-io
mkdir -p ~/minio/data
docker run -d \
   -p 9000:9000 \
   -p 9001:9001 \
   --name minio \
   -v ~/minio/data:/data \
   -e "MINIO_ROOT_USER=vijay" \
   -e "MINIO_ROOT_PASSWORD=donthireddy" \
   quay.io/minio/minio server /data --console-address ":9001"

# for spark run the following code before the spark-submit
export SPARK_LOCAL_IP="127.0.0.1"

# submit the jar file to spark
cd ~/code/spark-3.5.5-bin-hadoop3/
./bin/spark-submit --class=com.niharsystems.Main /Users/donthireddy/code/mygit/databricks-simple/target/databricks-simple-1.0-SNAPSHOT-jar-with-dependencies.jar

