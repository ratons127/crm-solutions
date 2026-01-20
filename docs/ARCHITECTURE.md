# Betopia HRM — Full System Guide

## What this system is
A multi‑tenant HRM platform with CDC (change data capture) ingestion so client attendance devices can stream data into a central HRM database.

## High‑level diagram

```
                        +-------------------------+
                        |  Client Attendance DB   |
                        |  (Postgres or MSSQL)     |
                        +-----------+-------------+
                                    |
                           CDC (WAL / SQL Server CDC)
                                    |
                           +--------v--------+
                           | Debezium Connect |
                           +--------+--------+
                                    |
                              Kafka Topics
                         tenant_<key>.*
                                    |
                           +--------v--------+
                           | Ingestion (Node) |
                           +--------+--------+
                                    |
                           +--------v--------+
                           | Central Postgres |
                           | schema per tenant|
                           +--------+--------+
                                    |
               +--------------------+--------------------+
               |                                         |
      +--------v--------+                       +--------v--------+
      |  Backend API     |                       |   Frontend UI    |
      |  (Spring Boot)   |                       |   (Next.js)      |
      +--------+--------+                       +--------+--------+
               |                                         |
               +----------------- Traefik ---------------+
                               (HTTPS)
```

## Multi‑tenant model
We use **schema‑per‑tenant** in the central Postgres:
- `tenant_<key>.cdc_events` stores CDC events
- The HRM app enforces tenant isolation in the API layer

## Core stacks

### 1) HRM App (core)
- Traefik reverse proxy with Let’s Encrypt
- Spring Boot backend
- Next.js frontend
- Central Postgres

### 2) CDC Stack (multi‑tenant)
- Kafka KRaft (3 brokers)
- Debezium Connect
- Kafka UI
- Debezium UI
- Ingestion service (Node.js)

## Topics and routing

Debezium writes events to topics:

```
tenant_<key>.<schema>.<table>
```

The ingestion service reads all `tenant_*` topics and writes into:

```
<tenant_schema>.cdc_events
```

## How to deploy (from scratch)

1) Clone repo and run:
```
./deploy.sh
```

2) Add DNS A records (all pointing to your server IP):
- `<FRONTEND_DOMAIN>`
- `api.<FRONTEND_DOMAIN>`
- `kafka.<FRONTEND_DOMAIN>`
- `debezium.<FRONTEND_DOMAIN>`

3) Wait for Let’s Encrypt to issue certificates.

## Onboarding a new client (Postgres)

### Client DB requirements
- `wal_level = logical`
- replication user

Example:
```
CREATE ROLE debezium WITH LOGIN REPLICATION PASSWORD 'strongpass';
GRANT CONNECT ON DATABASE clientdb TO debezium;
```

### Register connector
```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-postgres-connector.sh tenant1 10.0.0.10 5432 clientdb debezium strongpass
```

## Onboarding a new client (MSSQL)

### Client DB requirements
- SQL Server CDC enabled:
```
EXEC sys.sp_cdc_enable_db;
```

### Register connector
```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-mssql-connector.sh tenant1 10.0.0.10 1433 clientdb sa 'StrongPassword'
```

## Verify ingestion

```
docker exec -i hrm-postgres-1 psql -U betopia -d betopia_hrm \
  -c "SELECT * FROM tenant1.cdc_events ORDER BY id DESC LIMIT 5;"
```

## SMTP + Notifications
The backend sends password reset and email notifications through SMTP.
Configure SMTP in `.env` and restart the backend.

## Media uploads (S3)
If you want document uploads, configure AWS S3 keys in `.env`.

## Known components
- `deploy.sh`: prompts for config, writes `.env`, starts both stacks
- `OPERATIONS.md`: backups and operational commands
- `CODEX.md`: handoff and system history
