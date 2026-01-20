#!/usr/bin/env bash

echo "Connecting:-> http://localhost:8094"

curl -H "Accept:application/json" localhost:8094/connectors

#curl -H "Accept:application/json" localhost:8094/connectors/customer-mysql-connector-2
curl -H "Accept:application/json" localhost:8094/connectors/zetkco-mysql-connector-v1/status

echo "Success"