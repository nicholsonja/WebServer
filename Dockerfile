FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /src
COPY src/main/java/ .
RUN javac -d /classes edu/apsu/webserver/WebServer.java

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /classes/ .
EXPOSE 8000
CMD ["java", "edu.apsu.webserver.WebServer"]
