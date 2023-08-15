# Stage 1: Build the application
FROM adoptopenjdk:11-jdk-hotspot AS build
WORKDIR /app
# Copy your application source code and build it
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean
RUN ./gradlew build

# Stage 2: Create the final image
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/HomeManager.jar app.jar

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]




## Stage 1: Build the application
#FROM adoptopenjdk:11-jdk-hotspot AS build
#RUN chmod +x gradlew
#RUN ./gradlew build
#
## Stage 2: Create the final image
#FROM adoptopenjdk:11-jre-hotspot
## Copy the built JAR from the build stage
#COPY --from=build /app/build/libs/HomeManager.jar app.jar
#
## Specify the command to run your application
#CMD ["java", "-jar", "/build/libs/app.jar"]
