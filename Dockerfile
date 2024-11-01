# syntax=docker/dockerfile:1

FROM mcr.microsoft.com/openjdk/jdk:17-distroless

ARG VERSION=3.3.0

COPY --from=build ./ai.jar ai.jar

COPY ./target/spring-petclinic-$VERSION-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-javaagent:/ai.jar", "-jar", "/app.jar"]
