#!/bin/bash

sudo docker run -v /home/depromeet7/logs:/home/depromeet7/logs -p 5000:8080 -d --rm springboot
echo "done"
