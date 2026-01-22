# Stage 1: Build WAR
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run in Tomcat
FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

# Create directory for credentials
RUN mkdir -p /app/config

COPY --from=build /app/target/MVC_DETS.war /usr/local/tomcat/webapps/ROOT.war

# Handle Firebase credentials if provided as env variable
RUN echo '#!/bin/bash\n\
if [ -n "$FIREBASE_SERVICE_ACCOUNT_KEY" ]; then\n\
  echo "$FIREBASE_SERVICE_ACCOUNT_KEY" > /app/config/firebase-key.json\n\
  export GOOGLE_APPLICATION_CREDENTIALS=/app/config/firebase-key.json\n\
fi\n\
exec catalina.sh run' > /entrypoint.sh && chmod +x /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]