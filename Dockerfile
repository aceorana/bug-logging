# Use a small Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# App directory inside the container
WORKDIR /app

# Build arg so we don't hardcode the WAR name
# we now build a WAR, not a JAR
ARG WAR_FILE=target/*.war

# Copy the built WAR into the image as app.WAR
COPY ${WAR_FILE} app.war

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Start the app
ENTRYPOINT ["java","-jar","/app/app.war"]
