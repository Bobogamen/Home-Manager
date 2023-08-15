FROM ubuntu:lastest as build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

FROM openjdk-17-jdk-slim
EXPOSE 8080

COPY --from-build /build/libs/HomeManager.jar app.jar

ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]
