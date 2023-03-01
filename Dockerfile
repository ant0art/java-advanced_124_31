FROM openjdk:11-jre-slim

COPY ./app.jar /

ENTRYPOINT ["java", "-jar", "/app.jar"]
