#!/usr/bin/env bash
set -euo pipefail

if [ $# -lt 6 ]; then
  echo "Usage: $0 <tenant_key> <db_host> <db_port> <db_name> <db_user> <db_password>" >&2
  exit 1
fi

TENANT="$1"
DB_HOST="$2"
DB_PORT="$3"
DB_NAME="$4"
DB_USER="$5"
DB_PASSWORD="$6"
CONNECT_URL="${DEBEZIUM_CONNECT_URL:-http://localhost:18083}"
TOPIC_PREFIX="tenant_${TENANT}"
CONNECTOR_NAME="tenant_${TENANT}"

EXTRA_CONFIG=""
if [ -n "${TABLE_INCLUDE_LIST:-}" ]; then
  EXTRA_CONFIG="${EXTRA_CONFIG},\n      \"table.include.list\": \"${TABLE_INCLUDE_LIST}\""
fi

CONFIG=$(cat <<JSON
{
  "name": "${CONNECTOR_NAME}",
  "config": {
    "connector.class": "io.debezium.connector.sqlserver.SqlServerConnector",
    "tasks.max": "1",
    "database.hostname": "${DB_HOST}",
    "database.port": "${DB_PORT}",
    "database.user": "${DB_USER}",
    "database.password": "${DB_PASSWORD}",
    "database.names": "${DB_NAME}",
    "topic.prefix": "${TOPIC_PREFIX}",
    "database.encrypt": "false",
    "snapshot.mode": "initial",
    "tombstones.on.delete": "false",
    "decimal.handling.mode": "string",
    "time.precision.mode": "connect"
    ${EXTRA_CONFIG}
  }
}
JSON
)

curl -sS -X POST -H "Content-Type: application/json" \
  --data "${CONFIG}" \
  "${CONNECT_URL}/connectors" | tee /tmp/connector_${TENANT}.log
