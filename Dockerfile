FROM gradle:jdk18 AS build
VOLUME /tmp
COPY /build/libs/HomeManager.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]