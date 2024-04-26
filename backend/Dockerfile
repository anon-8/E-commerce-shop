FROM openjdk:21

RUN mkdir /backend

WORKDIR /backend

COPY target/backend-0.0.2-SNAPSHOT.jar spring-boot-app.jar

EXPOSE 8080

CMD ["java", "-jar", "spring-boot-app.jar"]