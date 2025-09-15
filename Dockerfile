FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:21-jre
COPY --from=builder /workspace/build/libs/eudi-srv-web-verifier-endpoint-23220-4-kt-0.2.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
