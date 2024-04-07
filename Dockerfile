# Note: Replace "adoptopenjdk/openjdk21:alpine-jre" with the actual image name for Java 21 on Alpine when available
FROM amazoncorretto:21-alpine-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file into the container
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} at-app.jar

EXPOSE 8081

# Specify the entry point to run the jar
ENTRYPOINT ["java", "-jar", "at-app.jar"]