
FROM openjdk:8-jdk-alpine
MAINTAINER kamesh.chauhan@gmail.com
VOLUME [ "/tmp" ]
ARG JAR_FILE
COPY ${JAR_FILE} /app/app.jar

#ENTRYPOINT [ "/usr/bin/java","-Djava.security.egd=file:/dev/./urandom -Xmx200m"]
CMD ["java", "-jar","-Djava.security.egd=file:/dev/./urandom -Xmx200m", "/app/app.jar"]
EXPOSE 5000
