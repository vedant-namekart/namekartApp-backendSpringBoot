# Use an official Maven image to build the app
FROM maven:3.8.4-openjdk-17-slim as builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and install dependencies (avoid copying unnecessary files)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the app
RUN mvn clean package -DskipTests

# Use an official OpenJDK image for runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=builder /app/target/my-springboot-api.jar /app/my-springboot-api.jar

# Expose the port your app will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "my-springboot-api.jar"]
