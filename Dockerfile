FROM openjdk:11-jdk as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM openjdk:11-jdk
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar /app.jar
ENV SERVER_PORT=8080
ENTRYPOINT ["java","--add-opens","java.base/java.lang=ALL-UNNAMED","-jar","/app.jar"]
