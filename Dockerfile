# Stage 1: Build the WAR using Maven
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first to leverage Docker layer caching
COPY pom.xml .

# Download dependencies (caching this layer separately)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the WAR
RUN mvn clean package -DskipTests

# Stage 2: Deploy on Tomcat
FROM tomcat:10.1-jdk17

# Clean default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from build stage and rename to ROOT.war
COPY --from=build /app/target/MVC_DETS.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Run Tomcat
CMD ["catalina.sh", "run"]
