#!/usr/bin/env bash

echo "Connecting:-> http://localhost:8094"

curl -H "Accept:application/json" localhost:8094/connectors
#curl -H "Accept:application/json" localhost:8094/connector-plugins

#curl -H "Accept:application/json" localhost:8094/connectors/{connector-name}/status
#curl -H "Accept:application/json" localhost:8094/connectors/{connector-name}
#curl -i -X DELETE -H "Accept:application/json" localhost:8094/connectors/{connector-name}

echo "Success"