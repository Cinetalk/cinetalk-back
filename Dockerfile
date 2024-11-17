# Dockerfile
FROM openjdk:21-jdk
ENV TZ=Asia/Seoul
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ./docker_test.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/docker_test.jar"]
