# Admin Guide (Kafka UI + pgAdmin)

## Kafka UI

### URL
- `https://kafka.<your-domain>`

### Login
- Username: value of `KAFKA_UI_USER` in `/opt/PRoject/hrm/.env`
- Password: value of `KAFKA_UI_PASSWORD` in `/opt/PRoject/hrm/.env`

### What to check
- **Topics**: you should see topics like `tenant_<key>.<schema>.<table>`
- **Consumers**: verify the ingestion service is consuming messages
- **Messages**: click a topic → view recent messages to confirm CDC is flowing

### Common actions
- Verify Debezium is producing events: check topic messages
- Monitor lag: consumer lag on ingestion group

---

## pgAdmin

### URL
- `https://pgadmin.<your-domain>`

### Login
- Email: value of `PGADMIN_EMAIL` in `/opt/PRoject/hrm/.env`
- Password: value of `PGADMIN_PASSWORD` in `/opt/PRoject/hrm/.env`

### Default server
The deploy script auto‑creates a server entry:
- **Name**: Betopia HRM Postgres
- **Host**: `postgres`
- **Port**: `5432`
- **DB**: `betopia_hrm`
- **User**: `betopia`

Password is stored in `pgadmin/.pgpass` when deployed.

### What to check
- **Schemas**: `tenant_<key>` schemas for each client
- **CDC events**: `tenant_<key>.cdc_events`

---

## Where to update credentials

```
/opt/PRoject/hrm/.env
```

After changing credentials, restart:

```
cd /opt/PRoject/hrm

docker compose up -d
```

---

## Troubleshooting quick tips

- **Kafka UI login fails**: check you updated the password in both `.env` files if you use both stacks:
  - `/opt/PRoject/hrm/.env`
  - `/opt/PRoject/hrm/multi-tenant-cdc/.env`

- **pgAdmin server missing**: reset pgAdmin data volume, then restart:
```
docker volume rm hrm_pgadmin_data
cd /opt/PRoject/hrm

docker compose up -d pgadmin
```
