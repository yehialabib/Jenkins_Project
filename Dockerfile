# Stage 1: Build stage
# We use the Gradle image with JDK 17 to compile the code
FROM gradle:9.4-jdk17 AS build
WORKDIR /app
# Copy the project files into the container
COPY . .
# Run the Gradle build (skipping tests here speed up the build, 
RUN gradle clean build -x test

# Stage 2: Run stage
# We use a lightweight JRE (Java Runtime Environment) for the final image
FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
WORKDIR /usr/app
# Gradle puts the compiled JAR in 'build/libs/' instead of 'target/'
# We use a wildcard (*) to catch the jar regardless of the version number
COPY --from=build /app/build/libs/*.jar /usr/app/app.jar
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]