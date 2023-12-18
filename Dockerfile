FROM eclipse-temurin:17 AS builder

WORKDIR /app

# Copy the dependencies
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

# Download the dependencies and their transitive dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package

# Runner image
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=builder /app/target/pw-distributed-monitoring-1.0.0-SNAPSHOT.jar /app/pw-distributed-monitoring-1.0.0-SNAPSHOT.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "pw-distributed-monitoring-1.0.0-SNAPSHOT.jar"]

# Set the default command
CMD ["--help"]
