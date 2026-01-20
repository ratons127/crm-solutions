#!/usr/bin/env bash
set -euo pipefail

if [ $# -ne 1 ]; then
  echo "Usage: $0 <tenant_key>" >&2
  exit 1
fi

TENANT="$1"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

psql -v tenant="$TENANT" -f "${SCRIPT_DIR}/bootstrap-tenant.sql"
