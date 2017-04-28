#!/bin/bash

#Preparing environment
echo "Compiling the TCF system"
mvn -q -DskipTests clean package
echo "Done"

# building the docker image
docker build --rm -t do_backend .
