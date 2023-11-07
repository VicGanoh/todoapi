FROM openjdk:19-jdk-alpine3.16
MAINTAINER vicgan
ARG JAR_FILE=target/todo-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} todoapi.jar
ENTRYPOINT ["java", "-jar", "/todoapi.jar"]
EXPOSE 9000