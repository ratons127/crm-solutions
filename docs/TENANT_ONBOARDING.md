# Tenant Onboarding Guide

This guide shows how to add a new client DB (Postgres or MSSQL) into the multi‑tenant CDC pipeline.

## Naming rules

Use a simple, lowercase tenant key with letters, numbers, and underscores only.
Example: `tenant_key = bdcalling`

CDC topics will be:
```
tenant_bdcalling.<schema>.<table>
```

Central storage will be:
```
tenant_bdcalling.cdc_events
```

## Postgres client onboarding

### 1) Enable logical replication
Edit `postgresql.conf` on the client DB:

```
wal_level = logical
max_replication_slots = 10
max_wal_senders = 10
```

Restart Postgres.

### 2) Create replication user

```
CREATE ROLE debezium WITH LOGIN REPLICATION PASSWORD 'strongpass';
GRANT CONNECT ON DATABASE clientdb TO debezium;
```

### 3) Register Debezium connector

On the HRM server:
```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-postgres-connector.sh tenant_key 10.0.0.10 5432 clientdb debezium strongpass
```

Optional filters:
```
export SCHEMA_INCLUDE_LIST=public
export TABLE_INCLUDE_LIST=public.attendance,public.users
```

### 4) Verify ingestion

```
docker exec -i hrm-postgres-1 psql -U betopia -d betopia_hrm \
  -c "SELECT * FROM tenant_key.cdc_events ORDER BY id DESC LIMIT 5;"
```

## SQL Server (MSSQL) client onboarding

### 1) Enable CDC on SQL Server

Run on the client DB:

```
EXEC sys.sp_cdc_enable_db;
```

Enable CDC per table:

```
EXEC sys.sp_cdc_enable_table
  @source_schema = N'dbo',
  @source_name   = N'Attendance',
  @role_name     = NULL,
  @supports_net_changes = 0;
```

### 2) Register Debezium SQL Server connector

On the HRM server:

```
cd /opt/PRoject/hrm/multi-tenant-cdc
DEBEZIUM_CONNECT_URL=http://127.0.0.1:18083 \
./scripts/register-mssql-connector.sh tenant_key 10.0.0.10 1433 clientdb sa 'StrongPassword'
```

### 3) Verify ingestion

```
docker exec -i hrm-postgres-1 psql -U betopia -d betopia_hrm \
  -c "SELECT * FROM tenant_key.cdc_events ORDER BY id DESC LIMIT 5;"
```

## Debezium UI steps (manual)

If you use the UI instead of the script, fill:
- Connector name: `tenant_<key>`
- Topic prefix: `tenant_<key>`
- Kafka brokers: `kafka-1:9092,kafka-2:9092,kafka-3:9092`
- Database hostname/port/user/password
- Database name(s)

## Screenshots

Add your screenshots here (place files in `docs/images/`):

- Debezium UI connector form  
  `![Debezium UI connector](images/debezium-connector-form.png)`
- Kafka UI topic list  
  `![Kafka UI topics](images/kafka-topics.png)`
- CDC events in central Postgres  
  `![CDC events](images/tenant-cdc-events.png)`
