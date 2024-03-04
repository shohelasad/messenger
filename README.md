## Take home task

## Prerequisites

* Docker 19.03.x (for production level readiness)
* Docker Compose 1.25.x
* Maven 3

## Used Technologies
* Spring Boot 3
* Spring Data
* Lombok
* Actuator
* Postgresql
* Apache Kafka
* Websocket

## How to run

### Run only test cases (Run docker before because Cache Service need to test with Test container)

```sh
mvn test
```

### Package the application as a JAR file

```sh
mvn clean install -DskipTests
```

### Run the Spring Boot application, Test server and Redis server together

```sh
docker-compose up -d
```

### Run the Spring Boot application

```sh
mvn spring-boot:run
```

Or, run as Java -jar

```sh
java -jar target/messenger-0.0.1-SNAPSHOT.jar
```