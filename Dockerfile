# Use the official OpenJDK image as the base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR/WAR file into the container
COPY . .

# Make the Gradle wrapper script executable
RUN chmod +x gradlew

#Run command for Gradle
RUN ./gradlew clean build

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
