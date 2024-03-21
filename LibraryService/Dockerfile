FROM ghcr.io/graalvm/jdk-community:21

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/libraryservice.jar app.jar

# Specify the command to run on container startup
CMD ["java", "-jar", "app.jar"]