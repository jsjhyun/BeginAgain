FROM openjdk:22-jdk

ARG FILE_NAME=board-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=build/libs/${FILE_NAME}

COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "/app.jar"]