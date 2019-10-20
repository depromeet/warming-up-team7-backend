#!/bin/bash

INAME='springboot'
CID=$(sudo docker ps | grep $INAME | awk '{print $1}')
echo "previous docker container id: [$CID]"

echo "try to remove..."
sudo docker stop $CID

echo "done"

