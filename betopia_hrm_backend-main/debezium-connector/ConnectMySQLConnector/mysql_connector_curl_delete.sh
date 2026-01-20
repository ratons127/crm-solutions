#!/usr/bin/env bash

echo "Connecting:-> http://localhost:8094"

curl -H "Accept:application/json" localhost:8094/connectors

curl -i -X DELETE -H "Accept:application/json" localhost:8094/connectors/zetkco-mysql-connector-v1

echo "Success"