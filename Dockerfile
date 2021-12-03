FROM openjdk:11-jre-slim

WORKDIR /app
COPY ./target/power-manager-0.0.2-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "power-manager-0.0.2-SNAPSHOT.jar"]
