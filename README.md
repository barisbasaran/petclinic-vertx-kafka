# Pet Clinic with Vert.x and Kafka 

## About 

This is an example **Java** RESTful web service for a **pet clinic**.
It is based on [Vert.x](https://vertx.io/)
and [Apache Kafka](https://kafka.apache.org/).

The whole list of tools used is as follows:
* [Vert.x](https://vertx.io/)
* [Apache Kafka](https://kafka.apache.org/)
* [Docker](https://www.docker.com)
* [Lombok](https://projectlombok.org)
* [Maven](https://maven.apache.org)

For testing:
* [JUnit](https://junit.org/junit5/)
* [AssertJ](https://assertj.github.io/doc/)

## Setup

### Build project

Run `mvn package` to build project with _Maven_.


### Start the application

Run `docker-compose --profile local up` to start the application with _Docker_.

To check that your application is running enter url `http://localhost:8080/`

### Start only Kafka

You may want to start only Kafka when you want to start the application separately from your IDE. 
Then run the following.

Run `docker-compose up` to start Kafka with _Docker_.

### Start the application in IDE

Run the main method of class **MainVerticle**.

## Endpoints

`GET /pets` Get all pets

`PUT /pets` Create pet

`GET /pets/{pet_id}` Get pet

`POST /pets/{pet_id}` Update pet
