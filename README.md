## Take home task

## Approach

* Implement a Spring Boot REST API to handle user creation and message sending functionalities.
* Incorporate a PostgreSQL database system to store messages persistently.
* Integrate Apache Kafka into the system to facilitate message communication between users. Configure Apache Kafka with partitions and replicas for scalability and fault tolerance.
* Enable partitioning to maintain the order of messages. The producer service generates a partition number using the hash code of the sender ID and receiver ID, ensuring that messages are distributed across partitions effectively.
* Implement a consumer service to listen for messages from the Kafka topic and deliver them to the corresponding users.
* Integrate WebSocket functionality into the consumer service to enable real-time message delivery to users.
* To scale the application further, additional consumers can be added, each assigned to handle messages from specific partitions, ensuring efficient processing and load balancing.

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

### Run only test cases

```sh
mvn test
```

### Package the application as a JAR file

```sh
mvn clean install -DskipTests
```

### Run the Kafka with Zookeeper and Postgresql

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