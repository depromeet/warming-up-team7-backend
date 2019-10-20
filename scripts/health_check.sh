#!/bin/bash

sleep 30s

RESPONSE=$(curl --write-out %{http_code} --silent --output /dev/null "localhost:5000/l7_health_check")
if [ $RESPONSE -eq 200 ]; then
	echo 'server is running'
	exit 0
fi
echo "something wrong. http status code:[$RESPONSE]"
exit 1
