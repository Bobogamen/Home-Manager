# Use the official OpenJDK image as the base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR/WAR file into the container
COPY . .

# Run Gradle to build the application
RUN chmod +x gradlew
RUN ./gradlew build --debug

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "build/libs/app.jar"]
