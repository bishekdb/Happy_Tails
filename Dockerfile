# Use Maven image to build, then OpenJDK to run
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (for better caching)
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK for runtime
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/website-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]