FROM openjdk:18

WORKDIR /app

COPY target/petclinic-vertx-kafka-1.0-SNAPSHOT.jar ./libs/
COPY target/dependency ./libs/

CMD ["java", "-cp", "libs/*", "io.baris.example.MainVerticle"]
