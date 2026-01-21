# Betopia HRM Operations

This guide covers production steps to keep the app fully operational.

## 1) Service status

```
cd /opt/PRoject/hrm && docker compose ps
cd /opt/PRoject/hrm/multi-tenant-cdc && docker compose ps
```

## 2) DNS + HTTPS

Ensure these A records point to `192.200.99.154`:
- `betopialtd.com`
- `api.betopialtd.com`
- `kafka.betopialtd.com`
- `debezium.betopialtd.com`

## 3) Secrets and environment

Edit `/opt/PRoject/hrm/.env`:
- `POSTGRES_PASSWORD`
- `CLOUD_AWS_*` (required for uploads)
- `SPRING_MAIL_*` (required for email)

Edit `/opt/PRoject/hrm/multi-tenant-cdc/.env`:
- `PGPASSWORD` (same as HRM DB)
- `KAFKA_UI_PASSWORD`

## 4) Super admin login

Use the admin login and immediately change the password:
- `admin@betopialtd.com / Admin@12345`

## 5) Prepare each client Postgres

On the client DB:
- set `wal_level=logical`
- create a replication user

Example:
```
CREATE ROLE debezium WITH LOGIN REPLICATION PASSWORD 'strongpass';
GRANT CONNECT ON DATABASE clientdb TO debezium;
```

## 6) Register Debezium connectors

```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-postgres-connector.sh tenant1 10.0.0.10 5432 clientdb debezium strongpass
```

Optional filters:
```
export SCHEMA_INCLUDE_LIST=public
export TABLE_INCLUDE_LIST=public.attendance,public.users
```

## 7) Verify ingestion

Insert a row in client DB, then check central DB:
```
docker exec -i hrm-postgres-1 psql -U betopia -d betopia_hrm \
  -c "SELECT * FROM tenant1.cdc_events ORDER BY id DESC LIMIT 5;"
```

## 8) Backups

Run a manual full backup:
```
/opt/PRoject/hrm/scripts/backup-postgres-full.sh
```

Backups are stored in:
- `/opt/PRoject/hrm/backups`

Schedule:
- Daily logical snapshot (data-only): `02:00`
- Weekly full backup (schema+data): `Sunday 03:00`

Note: daily backups are logical snapshots, not WAL-based physical incremental backups.

## 9) Logs

```
docker logs -f hrm-backend-1
docker logs -f hrm-frontend-1
docker logs -f multi-tenant-cdc-debezium-connect-1
docker logs -f multi-tenant-cdc-ingestion-1
```

## 10) Load testing

Run the k6 load test:
```
/opt/PRoject/hrm/scripts/loadtest/run-loadtest.sh
```

Environment overrides:
```
BASE_API=https://api.example.com/api
IDENTIFIER=admin@example.com
PASSWORD=StrongPass
API_VUS=500
UI_VUS=25
AUTH_VUS=5
```
