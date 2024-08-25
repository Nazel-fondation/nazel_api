FROM openjdk:22-jdk

WORKDIR /app

COPY .env /app/.env
COPY build/libs/nazelAPI.jar /app/nazelAPI.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/nazelAPI.jar"]