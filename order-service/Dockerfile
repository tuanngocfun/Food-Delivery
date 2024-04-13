FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/order-service-0.0.1-SNAPSHOT.jar /app/order-service-0.0.1-SNAPSHOT.jar

# Define the command to run your application
CMD ["java", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]