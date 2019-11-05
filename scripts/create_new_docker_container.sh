#!/bin/bash

cd /home/ubuntu/deploy/current/build/libs
unzip familytalk-0.0.1.jar

echo "try to build docker container..."
sudo docker build --tag springboot .

echo "done" 
