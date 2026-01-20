#!/usr/bin/env bash
set -euo pipefail

if [ $# -lt 5 ]; then
  echo "Usage: $0 <tenant_key> <db_host> <db_port> <db_name> <db_user> [db_password]" >&2
  exit 1
fi

TENANT="$1"
DB_HOST="$2"
DB_PORT="$3"
DB_NAME="$4"
DB_USER="$5"
DB_PASSWORD="${6:-}"
CONNECT_URL="${DEBEZIUM_CONNECT_URL:-http://localhost:18083}"
TOPIC_PREFIX="tenant_${TENANT}"
CONNECTOR_NAME="tenant_${TENANT}"
SLOT_NAME="debezium_${TENANT}"
PUBLICATION_NAME="debezium_${TENANT}"

EXTRA_CONFIG=""
if [ -n "${SCHEMA_INCLUDE_LIST:-}" ]; then
  EXTRA_CONFIG="${EXTRA_CONFIG},\n      \"schema.include.list\": \"${SCHEMA_INCLUDE_LIST}\""
fi
if [ -n "${TABLE_INCLUDE_LIST:-}" ]; then
  EXTRA_CONFIG="${EXTRA_CONFIG},\n      \"table.include.list\": \"${TABLE_INCLUDE_LIST}\""
fi

CONFIG=$(cat <<JSON
{
  "name": "${CONNECTOR_NAME}",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "tasks.max": "1",
    "database.hostname": "${DB_HOST}",
    "database.port": "${DB_PORT}",
    "database.user": "${DB_USER}",
    "database.password": "${DB_PASSWORD}",
    "database.dbname": "${DB_NAME}",
    "topic.prefix": "${TOPIC_PREFIX}",
    "slot.name": "${SLOT_NAME}",
    "publication.name": "${PUBLICATION_NAME}",
    "plugin.name": "pgoutput",
    "publication.autocreate.mode": "filtered",
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
