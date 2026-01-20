const { Kafka, logLevel } = require("kafkajs");
const { Pool } = require("pg");

const env = process.env;
const kafkaBrokers = (env.KAFKA_BROKERS || "").split(",").map((item) => item.trim()).filter(Boolean);
const topicPattern = new RegExp(env.KAFKA_TOPIC_PATTERN || ".*");
const tenantPrefix = env.TENANT_PREFIX || "";
const tenants = new Set(
  (env.TENANTS || "")
    .split(",")
    .map((item) => item.trim())
    .filter(Boolean)
    .map((item) => normalizeTenant(item))
);
const autoCreateTenants = env.AUTO_CREATE_TENANTS !== "false";

if (kafkaBrokers.length === 0) {
  console.error("KAFKA_BROKERS is required");
  process.exit(1);
}

const pool = new Pool({
  host: env.PGHOST,
  port: env.PGPORT ? Number(env.PGPORT) : 5432,
  database: env.PGDATABASE,
  user: env.PGUSER,
  password: env.PGPASSWORD,
  ssl: env.PGSSL === "true" ? { rejectUnauthorized: false } : undefined,
});

const kafka = new Kafka({
  clientId: env.KAFKA_CLIENT_ID || "ingestion-service",
  brokers: kafkaBrokers,
  logLevel: logLevel.INFO,
});

const consumer = kafka.consumer({ groupId: env.KAFKA_GROUP_ID || "ingestion-service" });
const tenantInit = new Map();

function normalizeTenant(value) {
  return value.toLowerCase().replace(/[^a-z0-9_]/g, "_");
}

function getTenantFromTopic(topic) {
  if (!topicPattern.test(topic)) {
    return null;
  }

  const [rawPrefix] = topic.split(".");
  if (!rawPrefix) {
    return null;
  }

  const withoutPrefix = tenantPrefix && rawPrefix.startsWith(tenantPrefix)
    ? rawPrefix.slice(tenantPrefix.length)
    : rawPrefix;

  const tenant = normalizeTenant(withoutPrefix);
  return tenant || null;
}

async function ensureTenant(tenant) {
  if (tenantInit.has(tenant)) {
    return tenantInit.get(tenant);
  }

  const initPromise = (async () => {
    const schemaName = `"${tenant}"`;
    await pool.query(`CREATE SCHEMA IF NOT EXISTS ${schemaName}`);
    await pool.query(
      `CREATE TABLE IF NOT EXISTS ${schemaName}.cdc_events (
        id BIGSERIAL PRIMARY KEY,
        source_topic TEXT NOT NULL,
        source_db TEXT,
        source_schema TEXT,
        source_table TEXT,
        op TEXT,
        ts_ms BIGINT,
        payload JSONB NOT NULL,
        inserted_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
      )`
    );
  })();

  tenantInit.set(tenant, initPromise);
  return initPromise;
}

function parseMessageValue(message) {
  if (!message || !message.value) {
    return null;
  }

  const raw = message.value.toString();
  if (!raw) {
    return null;
  }

  try {
    return JSON.parse(raw);
  } catch (error) {
    console.warn("Skipping non-JSON message", { error: error.message });
    return null;
  }
}

async function precreateTenants() {
  if (!tenants.size) {
    return;
  }

  for (const tenant of tenants) {
    await ensureTenant(tenant);
  }
}

async function run() {
  await precreateTenants();
  await consumer.connect();
  await consumer.subscribe({ topic: topicPattern, fromBeginning: false });

  await consumer.run({
    eachMessage: async ({ topic, message }) => {
      const tenant = getTenantFromTopic(topic);
      if (!tenant) {
        return;
      }

      if (!tenants.has(tenant) && !autoCreateTenants) {
        return;
      }

      await ensureTenant(tenant);

      const value = parseMessageValue(message);
      if (!value) {
        return;
      }

      const payload = value.payload || value;
      if (!payload) {
        return;
      }

      const source = payload.source || {};
      const sourceDb = source.db || null;
      const sourceSchema = source.schema || null;
      const sourceTable = source.table || null;
      const op = payload.op || null;
      const tsMs = payload.ts_ms || null;

      await pool.query(
        `INSERT INTO "${tenant}".cdc_events
          (source_topic, source_db, source_schema, source_table, op, ts_ms, payload)
         VALUES ($1, $2, $3, $4, $5, $6, $7)`,
        [topic, sourceDb, sourceSchema, sourceTable, op, tsMs, payload]
      );
    },
  });
}

run().catch((error) => {
  console.error("Ingestion service failed", error);
  process.exit(1);
});
