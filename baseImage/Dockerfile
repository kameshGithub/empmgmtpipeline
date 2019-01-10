## Docker file to create base image with java and JCE
FROM alpine:edge
MAINTAINER kamesh.chauhan@gmail.com
RUN apk add --no-cache openjdk8
COPY UnlimitedJCEPolicyJDK8/*  /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/
