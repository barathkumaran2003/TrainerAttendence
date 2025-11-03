# Use Maven + JDK image to build the jar first
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml and download dependencies (cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Package the app
RUN mvn clean package -DskipTests

# Second stage: use a lightweight JDK image to run the app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8010

ENTRYPOINT ["java", "-jar", "app.jar"]
