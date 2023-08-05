FROM gradle:7.2.0-jdk17 AS builder

WORKDIR /app

COPY . /app

RUN ./gradlew clean build

FROM amazoncorretto:17

WORKDIR /app

COPY --from=builder /app/build/libs/domains-app-0.0.1-SNAPSHOT.jar domains-app-0.0.1-SNAPSHOT.jar
EXPOSE 8080

ENV JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

CMD java $JAVA_OPTS -jar domains-app-0.0.1-SNAPSHOT.jar