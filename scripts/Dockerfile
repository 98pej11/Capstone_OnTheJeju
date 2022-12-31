
FROM openjdk:11-jdk

WORKDIR /backapp

#COPY . .

ARG JAR_FILE=build/libs/jejuTour-recommend-0.0.1-SNAPSHOT.jar

COPY jejuTour-recommend-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT ["java","-jar","app.jar"]
