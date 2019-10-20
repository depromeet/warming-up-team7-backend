#!/bin/bash

DIR_NAME=$(date +%y%m%d_%H%M%S)

echo "creating new dir:[$DIR_NAME] ..."
mkdir /home/ubuntu/deploy/$DIR_NAME

echo 'moving artifacts ...'
mv -v /home/ubuntu/deploy/current/* /home/ubuntu/deploy/$DIR_NAME
