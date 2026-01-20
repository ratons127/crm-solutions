#!/usr/bin/env bash
set -euo pipefail

CRON_FILE="/etc/cron.d/hrm-backup"
DAILY_CMD="/opt/PRoject/hrm/scripts/backup-postgres-daily.sh"
WEEKLY_CMD="/opt/PRoject/hrm/scripts/backup-postgres-full.sh"

if [ ! -x "${DAILY_CMD}" ]; then
  echo "Daily backup script not found or not executable: ${DAILY_CMD}" >&2
  exit 1
fi

if [ ! -x "${WEEKLY_CMD}" ]; then
  echo "Weekly backup script not found or not executable: ${WEEKLY_CMD}" >&2
  exit 1
fi

cat <<CRON | sudo tee "${CRON_FILE}" > /dev/null
SHELL=/bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

0 2 * * * root ${DAILY_CMD} >> /var/log/hrm-backup.log 2>&1
0 3 * * 0 root ${WEEKLY_CMD} >> /var/log/hrm-backup.log 2>&1
CRON

sudo chmod 0644 "${CRON_FILE}"

echo "Cron installed: ${CRON_FILE} (daily 02:00, weekly full Sunday 03:00)"
