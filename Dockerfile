# Stage 1: Build stage
FROM openjdk:17-alpine AS build
WORKDIR /app
COPY target/VirtualTapcash-0.0.1-SNAPSHOT.jar /app/VirtualTapcash.jar
EXPOSE 8080

# Stage 2: Runtime stage
FROM openjdk:17-alpine
ARG PORT
ENV PORT=${PORT}
COPY --from=build /app/VirtualTapcash.jar .
RUN adduser -D runtime
USER runtime
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "VirtualTapcash.jar"]

