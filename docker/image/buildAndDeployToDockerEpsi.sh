#!/bin/bash

# Build
./gradlew build

# Delete files from server
ssh root@192.168.100.7 /docker/configuration/ws_foldermanagement/image/clearTarget.sh

# Copy files to server
scp docker/image/makeWSDockerImage.sh  root@192.168.100.7:/docker/configuration/ws_foldermanagement/image/
scp docker/image/Dockerfile  root@192.168.100.7:/docker/configuration/ws_foldermanagement/image/
scp docker/image/target/application.properties  root@192.168.100.7:/docker/configuration/ws_foldermanagement/image/target/
scp docker/image/target/log4j2-spring.xml  root@192.168.100.7:/docker/configuration/ws_foldermanagement/image/target/
scp build/libs/wsfoldermanagement-0.1.0.jar root@192.168.100.7:/docker/configuration/ws_foldermanagement/image/target/ws-foldermanagement.jar

# Rebuild docker image
ssh root@192.168.100.7 /docker/configuration/ws_foldermanagement/image/makeWSDockerImage.sh