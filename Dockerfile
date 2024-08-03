FROM registry.access.redhat.com/ubi8/openjdk-17
WORKDIR /app
COPY target/homework-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
