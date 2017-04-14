#!/bin/bash

./compile.sh

docker build --rm -t dotnet_server .

rm -rf server.exe
