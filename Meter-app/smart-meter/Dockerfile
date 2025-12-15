# Base image
FROM eclipse-temurin:17-jdk


# Set working directory
WORKDIR /app

# Copy the jar into the container
COPY target/smart-meter-0.0.1-SNAPSHOT.jar smart-meter.jar

# Expose port 
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "smart-meter.jar"]
