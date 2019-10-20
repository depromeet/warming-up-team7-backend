#!/bin/bash

cd /home/ubuntu/deploy/current

echo "try to build docker container..."
sudo docker build --tag springboot .

echo "done" 
