#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

PURGE="false"
if [ "${1:-}" = "--purge" ]; then
  PURGE="true"
fi

cd "${ROOT_DIR}"

if [ "$PURGE" = "true" ]; then
  echo "Stopping and removing core stack (with volumes)..."
  docker compose down -v
else
  echo "Stopping core stack (keeping volumes)..."
  docker compose down
fi

if [ -d "${ROOT_DIR}/multi-tenant-cdc" ]; then
  cd "${ROOT_DIR}/multi-tenant-cdc"
  if [ "$PURGE" = "true" ]; then
    echo "Stopping and removing CDC stack (with volumes)..."
    docker compose down -v
  else
    echo "Stopping CDC stack (keeping volumes)..."
    docker compose down
  fi
fi

if [ "$PURGE" = "true" ]; then
  echo "Purge complete. All volumes removed."
else
  echo "Done. Volumes were kept. Use --purge to remove data."
fi
