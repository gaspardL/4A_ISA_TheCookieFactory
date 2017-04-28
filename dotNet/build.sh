#!/bin/bash

./compile.sh

docker build --rm -t do_dotnet_server .

rm -rf server.exe
