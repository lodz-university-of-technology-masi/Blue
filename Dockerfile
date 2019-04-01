# Docker multi-stage build

# 1. Building the App with Maven
FROM maven:3-jdk-11 AS builder

ADD . /rekrutacjaApp
WORKDIR /rekrutacjaApp

# Run Maven build
RUN mvn clean install

# Just using the build artifact and then removing the build-container
FROM openjdk:11-jdk

VOLUME /tmp

# Add Spring Boot app.jar to Container
COPY --from=builder "/rekrutacjaApp/backend/target/backend-0.0.1-SNAPSHOT.jar" app.jar

ENV JAVA_OPTS=""

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]