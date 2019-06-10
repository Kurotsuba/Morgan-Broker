FROM openjdk:8-jdk-alpine
LABEL maintainer="kanneco98@gmail.com"
VOLUME /tmp
EXPOSE 8011
ARG JAR_FILE=target/${project.build.finalname}-30405.jar
ADD ${JAR_FILE} broker-gateway.jar
ENTRYPOINT {"java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/broker-gateway.jar"}