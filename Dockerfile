# jdk21 Image Start
FROM openjdk:21

# 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=notification

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
