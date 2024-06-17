FROM openjdk:21-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-Dspring.config.activate.on-profile=prod", "-jar", "/app.jar"]