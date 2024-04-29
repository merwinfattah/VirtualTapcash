# Use OpenJDK 17 as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Expose the port your application runs on
EXPOSE 8080

# Specify the command to run your application
CMD ["java", "-jar", "VirtualTapcash.jar"]
