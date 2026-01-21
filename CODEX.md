# Codex Handoff Notes (Betopia HRM)

## Overview
This server hosts a multi-tenant HRM application with CDC ingestion.

- Main app: Spring Boot backend + Next.js frontend behind Traefik (HTTPS)
- Central DB: Postgres (betopia_hrm)
- CDC stack (multi-tenant): Kafka (KRaft, 3 brokers) + Debezium Connect + ingestion service (Node.js)
- Legacy CDC stack (MySQL-based) is still running but not used for multi-tenant Postgres
- One-command deploy helper: `/opt/PRoject/hrm/deploy.sh`
- Uninstall helper: `/opt/PRoject/hrm/uninstall.sh`
- pgAdmin available at `PGADMIN_DOMAIN` (configured in `.env`)

## What I changed

### Core app deployment
- Deployed Traefik reverse proxy with Let’s Encrypt.
- Deployed backend + frontend with domain routing:
  - `betopialtd.com` → frontend
  - `api.betopialtd.com` → backend
- Set production config in `/opt/PRoject/hrm/.env`.
- Updated frontend API URL in `/opt/PRoject/hrm/betopia_hrm_frontend-main/.env.example`.
- Updated backend config to read env variables for AWS/SMTP/DB.

### Super admin
- Created default admin account:
  - `admin@betopialtd.com / Admin@12345`

### Multi-tenant CDC stack (NEW)
- Added a separate compose project:
  - `/opt/PRoject/hrm/multi-tenant-cdc/docker-compose.yml`
- Stack includes:
  - Kafka KRaft (3 brokers)
  - Debezium Connect
  - Kafka UI
  - Debezium UI
  - Ingestion service (Node.js)
- Added HTTPS routing via Traefik:
  - `kafka.betopialtd.com`
  - `debezium.betopialtd.com`

### Ingestion service (NEW)
- Location: `/opt/PRoject/hrm/multi-tenant-cdc/ingestion`
- Consumes Debezium topics named `tenant_<key>.*`
- Writes events into schema-per-tenant tables in central Postgres.
- Table format per tenant:
  - `tenant_<key>.cdc_events`

### Tenant bootstrap
- Scripts:
  - `/opt/PRoject/hrm/multi-tenant-cdc/scripts/bootstrap-tenant.sh`
  - `/opt/PRoject/hrm/multi-tenant-cdc/scripts/register-postgres-connector.sh`
  - `/opt/PRoject/hrm/multi-tenant-cdc/scripts/register-mssql-connector.sh`
- Created initial schema:
  - `tenant_default`

### Operations + Backups
- Ops guide: `/opt/PRoject/hrm/OPERATIONS.md`
- Backup scripts:
  - `/opt/PRoject/hrm/scripts/backup-postgres-full.sh`
  - `/opt/PRoject/hrm/scripts/backup-postgres-daily.sh`
- Cron installer:
  - `/opt/PRoject/hrm/scripts/install-backup-cron.sh`
- Schedule:
  - Daily data-only dump at 02:00
  - Weekly full dump Sunday 03:00
  - Retention: 30 days

### Load testing
- k6 scripts: `/opt/PRoject/hrm/scripts/loadtest`

## How the system works (high level)

1) Client DB (Postgres or MSSQL) receives attendance records.
2) Debezium reads CDC changes and writes to Kafka topics:
   - `tenant_<key>.<schema>.<table>`
3) Ingestion service reads Kafka topics and inserts rows into:
   - `tenant_<key>.cdc_events` in central Postgres
4) HRM backend API serves tenant data to frontend.

## Current status

- HRM app is live on:
  - `https://betopialtd.com`
  - `https://api.betopialtd.com`
- CDC stack is live on:
  - `https://kafka.betopialtd.com`
  - `https://debezium.betopialtd.com`
- SMTP configured with Gmail (no-reply@valrpro.com).
- AWS S3 configured (bucket: valrpro-media, region: us-east-1).
- AWS CLI v2 installed at `/root/.local/bin/aws` for S3 testing.

## What still needs to be done

### Required before onboarding clients
- Register at least one Debezium connector for a client DB.
- For client Postgres:
  - `wal_level=logical`
  - Create replication user
- For client MSSQL:
  - CDC must be enabled
  - Use Debezium SQL Server connector (script available)

### Optional improvements
- Replace placeholder AWS keys in `/opt/PRoject/hrm/.env` if using uploads.
- Add monitoring alerts (Prometheus/Grafana).
- Add WAL-based physical backups if required.

## How to add a new client (Postgres)

```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-postgres-connector.sh tenant1 10.0.0.10 5432 clientdb debezium strongpass
```

## How to add a new client (MSSQL)

```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-mssql-connector.sh tenant1 10.0.0.10 1433 clientdb sa 'StrongPassword'
```

## How to check CDC ingestion

```
docker exec -i hrm-postgres-1 psql -U betopia -d betopia_hrm \
  -c "SELECT * FROM tenant1.cdc_events ORDER BY id DESC LIMIT 5;"
```

## SMTP/S3 test notes

- SMTP test used a temporary user `iraton356@gmail.com` to trigger the forgot-password flow.
- S3 test uploaded a small object to `valrpro-media` successfully.

## One-command deployment

Run from the repo root:

```
./deploy.sh
```

The script prompts for domains, SMTP, AWS, and DB secrets, generates `.env` files,
updates the frontend API URL, and starts both the core stack and CDC stack.

## Documentation

- `README.md` for quick start
- `docs/ARCHITECTURE.md` for full system details and diagrams

## Useful commands

```
# Main app
cd /opt/PRoject/hrm

docker compose ps

docker compose logs -f backend

# CDC stack
cd /opt/PRoject/hrm/multi-tenant-cdc

docker compose ps

docker compose logs -f ingestion
```
