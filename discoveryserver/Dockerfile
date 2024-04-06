FROM ghcr.io/graalvm/jdk-community:21 AS build

WORKDIR /app

COPY build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]