# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and config
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy the rest of your project
COPY src ./src

# Make mvnw executable
RUN chmod +x mvnw

# Build the application (skip tests to speed up)
RUN ./mvnw clean package -DskipTests

# Expose port (Render will map it dynamically via $PORT)
EXPOSE 8080

# Run the Spring Boot jar
CMD ["sh", "-c", "java -jar target/pannel2-0.0.1-SNAPSHOT.jar"]
