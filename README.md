# Betopia HRM (Multi‑Tenant + CDC)

This repo ships a production-ready HRM app plus a multi-tenant CDC ingestion stack.

## Quick start (one command)

```
./deploy.sh
```

The script will:
- prompt for domains, DB credentials, SMTP, AWS S3, and Kafka UI credentials
- generate `.env` files for both stacks
- update the frontend API URL
- start the core app and CDC stack via Docker Compose

## Architecture (summary)

- Frontend: Next.js
- Backend: Spring Boot
- Central DB: Postgres
- CDC stack: Kafka (KRaft) + Debezium Connect + Ingestion service (Node.js)

See full details and diagrams in `docs/ARCHITECTURE.md`.

## Core services

- Frontend: `https://<FRONTEND_DOMAIN>`
- API: `https://<API_DOMAIN>`
- Kafka UI: `https://<KAFKA_UI_DOMAIN>`
- Debezium UI: `https://<DEBEZIUM_UI_DOMAIN>`

## Important files

- Deployment guide: `deploy.sh`
- Ops guide: `OPERATIONS.md`
- Full architecture + onboarding: `docs/ARCHITECTURE.md`
- Handoff notes: `CODEX.md`

## Security

- Do NOT commit `.env` or any secrets.
- Rotate any secrets if they were ever shared.

## Uninstall

```
./uninstall.sh
```

By default, this stops containers but **keeps data**. You can pass `--purge` to remove volumes.
