FROM maven:3-eclipse-temurin-23-alpine

WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]


#COPY ./target/DockerWSpring-0.0.1-SNAPSHOT.jar .
#
#CMD ["java", "-jar", "DockerWSpring-0.0.1-SNAPSHOT.jar"]
#
#EXPOSE 8080


