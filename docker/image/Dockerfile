# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="jlb.epsi@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
# EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/ws-foldermanagement.jar

# Add the application's jar to the container
COPY ${JAR_FILE} foldermanagement.jar
# Add dependencies
COPY target/application.properties application.properties
COPY target/log4j2-spring.xml log4j2-spring.xml

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/foldermanagement.jar"]
