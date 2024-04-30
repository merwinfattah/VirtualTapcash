# Use OpenJDK 17 as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built by Maven to the container
COPY target/VirtualTapcash-0.0.1-SNAPSHOT.jar /app/VirtualTapcash.jar

# Expose the port your application runs on
EXPOSE 8080

# Specify the command to run your application

CMD ["java", "-jar", "VirtualTapcash.jar"]
