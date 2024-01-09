
FROM openjdk:17-jdk-slim
WORKDIR /code
COPY ./target/helloApi-1.0-SNAPSHOT.jar ./hw.jar
RUN apt-get -y update; apt-get -y install curl
EXPOSE 8080
CMD ["java", "-jar", "hw.jar"]
