
FROM openjdk:8-jdk-alpine
MAINTAINER kamesh.chauhan@gmail.com
VOLUME [ "/tmp" ]
ADD ./target/emp-mgmt-0.0.1-SNAPSHOT.jar /app/

#ENTRYPOINT [ "/usr/bin/java","-Djava.security.egd=file:/dev/./urandom -Xmx200m"]
CMD ["/user/bin/java", "-jar","-Djava.security.egd=file:/dev/./urandom -Xmx200m", "/app/emp-mgmt-0.0.1-SNAPSHOT.jar"]
EXPOSE 5000
