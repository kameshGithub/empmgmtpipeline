
FROM openjdk:8-jdk-alpine
MAINTAINER kamesh.chauhan@gmail.com
VOLUME [ "/tmp" ]
#ARG JAR_FILE
#COPY ${JAR_FILE} /app/app.jar
#RUN echo ${JAR_FILE}
COPY ./target/empmgmtbe-0.0.1.jar /app/
#ENTRYPOINT [ "/usr/bin/java","-Djava.security.egd=file:/dev/./urandom -Xmx200m"]
#CMD ["java", "-jar","-Djava.security.egd=file:/dev/./urandom -Xmx200m", "/app/app.jar"]
CMD ["java", "-jar","-Djava.security.egd=file:/dev/./urandom -Xmx200m", "/app/empmgmtbe-0.0.1.jar"]
EXPOSE 5000
