#!/usr/bin/env bash

var="$(cat mysql_connector.json)"
echo "$var"

#var_2= '{ "name": "customer-mysql-connector-2", "config": { "connector.class": "io.debezium.connector.mysql.MySqlConnector", "tasks.max": "1", "offset.storage": "org.apache.kafka.connect.storage.FileOffsetBackingStore", "offset.storage.file.filename": "/tmp/offsets.dat", "offset.flush.interval.ms": "60000", "database.hostname": "mysql-db-source", "database.port": "3306", "database.user": "root", "database.password": "root@123", "database.dbname": "sourceDB", "include.schema.changes": "true", "database.server.id": "10182", "database.server.name": "customer-mysql-db-server-2", "database.allowPublicKeyRetrieval": "true", "database.history.kafka.bootstrap.servers": "kafka:9092", "database.history.kafka.topic": "schema-changes.test-2" } }'

echo "http://localhost:8094/connectors/"

#curl -i -X POST -H "Accept:application/json" \
#                -H "Content-Type:application/json" \
#                localhost:8094/connectors/ \
#                -d '\'' ' $var_2 '\'''

echo "Success"