
FROM alpine-java:base
MAINTAINER kamesh.chauhan@gmail.com
VOLUME [ "/tmp" ]
ADD ./target/emp-mgmt-0.0.1-SNAPSHOT.jar /app/

//ENTRYPOINT [ "/usr/bin/java","-Djava.security.egd=file:/dev/./urandom -Xmx200m"]
CMD ["-jar", "/app/emp-mgmt-0.0.1-SNAPSHOT.jar"]
EXPOSE 5000
