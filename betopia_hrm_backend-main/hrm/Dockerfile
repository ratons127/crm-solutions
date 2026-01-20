#FROM openjdk:17
#FROM ibm-semeru-runtimes:open-17-jdk
#MAINTAINER com.betopia-hrm.com
##
#ADD target/ROOT.jar ROOT.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "ROOT.jar"]

# Step 1: Build Stage (Maven দিয়ে build করার জন্য optional multi-stage)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
#
## Step 2: Runtime Stage (lightweight image)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
