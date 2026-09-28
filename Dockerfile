# Stage 1: Build the Spring Boot application
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom.xml first to leverage Docker layer caching
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user for security
RUN addgroup -S elearning && adduser -S elearning -G elearning

# Copy the JAR from the builder stage
COPY --from=builder /build/target/elearning-backend-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown elearning:elearning app.jar

USER elearning

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
