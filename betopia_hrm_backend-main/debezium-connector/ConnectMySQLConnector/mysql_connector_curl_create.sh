#!/usr/bin/env bash

domain="localhost:8094"
echo "Connecting:-> http://${domain}"

payload="$(cat mysql_connector.json)"
#echo ${payload}

curl -i -X POST -H "Accept:application/json" \
                -H "Content-Type:application/json" \
                ${domain}/connectors/ \
                -d "${payload}"

#end-of-scripts