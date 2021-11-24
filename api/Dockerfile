# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
# RUN ./mvnw spring-javaformat:apply
RUN mkdir -p filexio-files/kiryukan-AU7kXvsx

CMD ["./mvnw", "spring-boot:run"]

# Expose port 3009
# EXPOSE 3009