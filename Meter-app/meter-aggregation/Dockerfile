# Base image
FROM eclipse-temurin:17-jdk


# Set working directory
WORKDIR /app

# Copy the jar into the container
COPY target/meter-aggregation-0.0.1-SNAPSHOT.jar meter-aggregation.jar

# Expose port 
EXPOSE 8081

# Run the jar
ENTRYPOINT ["java", "-jar", "meter-aggregation.jar"]
