# Troubleshooting

Common issues and fixes.

## SSL certificate shows self‑signed

Cause: Let’s Encrypt failed due to DNS not propagated or rate limits.

Fix:
- Confirm A records for all domains point to the server.
- Wait for DNS to propagate.
- Traefik retries automatically; restart Traefik if needed.

## Debezium connector fails to start

Common causes:
- Wrong DB credentials or host blocked by firewall.
- CDC not enabled (SQL Server).
- Postgres not in `wal_level=logical`.

Check:
```
curl http://127.0.0.1:18083/connectors
curl http://127.0.0.1:18083/connectors/<name>/status
```

## No CDC events in central Postgres

Check:
- Connector status is RUNNING
- Topics exist in Kafka UI
- Ingestion service logs:
```
docker logs -f multi-tenant-cdc-ingestion-1
```

## Kafka brokers are restarting

Causes:
- Invalid KRaft cluster ID
- Port conflicts

Fix:
- Recreate KRaft ID via deploy script or:
```
docker run --rm confluentinc/cp-kafka:7.5.1 kafka-storage random-uuid
```
- Update `KAFKA_KRAFT_CLUSTER_ID` and restart the CDC stack.

## SMTP not sending emails

Check:
- SMTP credentials in `/opt/PRoject/hrm/.env`
- Backend restart after changes
- Gmail requires App Passwords (not account password)

## S3 upload fails

Check:
- Bucket name and region
- IAM permissions: `s3:PutObject`, `s3:GetObject`, `s3:ListBucket`
- Backend restart after changes

## Backend 502 / not reachable

Check:
- `docker logs hrm-backend-1`
- DB connection is valid
- Required env vars exist

## Forgot password returns OK but no email

- SMTP may be misconfigured
- Check backend logs for mail errors

