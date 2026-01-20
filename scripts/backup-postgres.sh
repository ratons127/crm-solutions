#!/usr/bin/env bash
set -euo pipefail

BACKUP_DIR="/opt/PRoject/hrm/backups"
CONTAINER_NAME="hrm-postgres-1"
DB_NAME="betopia_hrm"
DB_USER="betopia"

mkdir -p "${BACKUP_DIR}"

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_${TIMESTAMP}.sql.gz"
RETENTION_DAYS=30

if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "Postgres container ${CONTAINER_NAME} is not running" >&2
  exit 1
fi

# Use pg_dump inside the container; compress on host.
if ! docker exec -i "${CONTAINER_NAME}" pg_dump -U "${DB_USER}" "${DB_NAME}" | gzip > "${BACKUP_FILE}"; then
  echo "Backup failed" >&2
  exit 1
fi

echo "Backup created: ${BACKUP_FILE}"

# Retention cleanup
find "${BACKUP_DIR}" -type f -name "${DB_NAME}_*.sql.gz" -mtime "+${RETENTION_DAYS}" -print -delete
