#!/usr/bin/env bash

VERSION=2

mvn clean package
docker login
docker build -t nassos/cubesensors:$VERSION .
docker push nassos/cubesensors:$VERSION