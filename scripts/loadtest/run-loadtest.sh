#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

BASE_API=${BASE_API:-https://api.betopialtd.com/api}
KAFKA_UI=${KAFKA_UI:-https://kafka.betopialtd.com}
DEBEZIUM_UI=${DEBEZIUM_UI:-https://debezium.betopialtd.com}
IDENTIFIER=${IDENTIFIER:-admin@betopialtd.com}
PASSWORD=${PASSWORD:-Admin@12345}
DASHBOARD_PATH=${DASHBOARD_PATH:-/v1/cashAdvanceNotifications?page=1&perPage=1}

API_VUS=${API_VUS:-1000}
UI_VUS=${UI_VUS:-50}
AUTH_VUS=${AUTH_VUS:-10}

mkdir -p /tmp

if ! docker image inspect grafana/k6:latest >/dev/null 2>&1; then
  docker pull grafana/k6:latest
fi

docker run --rm -i \
  -e BASE_API="${BASE_API}" \
  -e KAFKA_UI="${KAFKA_UI}" \
  -e DEBEZIUM_UI="${DEBEZIUM_UI}" \
  -e IDENTIFIER="${IDENTIFIER}" \
  -e PASSWORD="${PASSWORD}" \
  -e DASHBOARD_PATH="${DASHBOARD_PATH}" \
  -e API_VUS="${API_VUS}" \
  -e UI_VUS="${UI_VUS}" \
  -e AUTH_VUS="${AUTH_VUS}" \
  -v "${SCRIPT_DIR}/k6-loadtest.js:/script.js" \
  -v /tmp:/tmp \
  grafana/k6 run --summary-export /tmp/k6-summary.json /script.js

cat /tmp/k6-summary.json > /tmp/k6-summary-latest.json
