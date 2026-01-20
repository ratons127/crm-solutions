#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

prompt() {
  local label="$1"
  local default_value="$2"
  local input
  if [ -n "$default_value" ]; then
    read -rp "${label} [${default_value}]: " input
    echo "${input:-$default_value}"
  else
    read -rp "${label}: " input
    echo "$input"
  fi
}

prompt_secret() {
  local label="$1"
  local input
  read -rsp "${label}: " input
  echo
  echo "$input"
}

SUDO=""
if [ "$(id -u)" -ne 0 ]; then
  SUDO="sudo"
fi

install_docker() {
  echo "Installing Docker (requires sudo)..."
  ${SUDO} apt-get update
  ${SUDO} apt-get install -y docker.io docker-compose-plugin
  ${SUDO} systemctl enable --now docker
  if [ "$(id -u)" -ne 0 ]; then
    ${SUDO} usermod -aG docker "${USER}" || true
    echo "You may need to log out/in to use docker without sudo."
  fi
}

if ! command -v docker >/dev/null 2>&1; then
  install_docker
fi

if ! docker compose version >/dev/null 2>&1; then
  install_docker
fi

PUBLIC_IP=$(curl -s https://api.ipify.org || true)

FRONTEND_DOMAIN=$(prompt "Frontend domain" "")
if [ -z "$FRONTEND_DOMAIN" ]; then
  echo "Frontend domain is required." >&2
  exit 1
fi

API_DOMAIN=$(prompt "API domain" "api.${FRONTEND_DOMAIN}")
KAFKA_UI_DOMAIN=$(prompt "Kafka UI domain" "kafka.${FRONTEND_DOMAIN}")
DEBEZIUM_UI_DOMAIN=$(prompt "Debezium UI domain" "debezium.${FRONTEND_DOMAIN}")
ACME_EMAIL=$(prompt "ACME email" "admin@${FRONTEND_DOMAIN}")

POSTGRES_DB=$(prompt "Postgres DB name" "betopia_hrm")
POSTGRES_USER=$(prompt "Postgres user" "betopia")
POSTGRES_PASSWORD=$(prompt_secret "Postgres password")

SMTP_HOST=$(prompt "SMTP host" "smtp.gmail.com")
SMTP_PORT=$(prompt "SMTP port" "587")
SMTP_USER=$(prompt "SMTP username" "")
SMTP_PASS=$(prompt_secret "SMTP password")
SMTP_FROM=$(prompt "SMTP from" "no-reply@${FRONTEND_DOMAIN}")

AWS_ACCESS_KEY=$(prompt "AWS Access Key ID" "")
AWS_SECRET_KEY=$(prompt_secret "AWS Secret Access Key")
AWS_REGION=$(prompt "AWS region" "us-east-1")
AWS_BUCKET=$(prompt "AWS S3 bucket" "")

KAFKA_EXTERNAL_HOST=$(prompt "Public IP/host for Kafka external" "${PUBLIC_IP}")
KAFKA_UI_USER=$(prompt "Kafka UI user" "admin")
KAFKA_UI_PASSWORD=$(prompt_secret "Kafka UI password")

MYSQL_CDC_DATABASE=$(prompt "MySQL CDC database (optional)" "betopia_hrm")
MYSQL_CDC_ROOT_PASSWORD=$(prompt_secret "MySQL CDC root password (optional)")

APP_FRONTEND_RESET_PASSWORD_URL="https://${FRONTEND_DOMAIN}/auth"

KAFKA_KRAFT_CLUSTER_ID=$(docker run --rm confluentinc/cp-kafka:7.5.1 kafka-storage random-uuid)

cat <<ENV > "${ROOT_DIR}/.env"
ACME_EMAIL=${ACME_EMAIL}
FRONTEND_DOMAIN=${FRONTEND_DOMAIN}
API_DOMAIN=${API_DOMAIN}

POSTGRES_DB=${POSTGRES_DB}
POSTGRES_USER=${POSTGRES_USER}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD}

SPRING_MAIL_HOST=${SMTP_HOST}
SPRING_MAIL_PORT=${SMTP_PORT}
SPRING_MAIL_USERNAME=${SMTP_USER}
SPRING_MAIL_PASSWORD=${SMTP_PASS}
SPRING_MAIL_FROM=${SMTP_FROM}

CLOUD_AWS_CREDENTIALS_ACCESS_KEY=${AWS_ACCESS_KEY}
CLOUD_AWS_CREDENTIALS_SECRET_KEY=${AWS_SECRET_KEY}
CLOUD_AWS_REGION=${AWS_REGION}
CLOUD_AWS_S3_BUCKET=${AWS_BUCKET}

APP_FRONTEND_RESET_PASSWORD_URL=${APP_FRONTEND_RESET_PASSWORD_URL}

MYSQL_CDC_DATABASE=${MYSQL_CDC_DATABASE}
MYSQL_CDC_ROOT_PASSWORD=${MYSQL_CDC_ROOT_PASSWORD}

KAFKA_EXTERNAL_HOST=${KAFKA_EXTERNAL_HOST}
KAFKA_UI_USER=${KAFKA_UI_USER}
KAFKA_UI_PASSWORD=${KAFKA_UI_PASSWORD}
ENV

cat <<ENV > "${ROOT_DIR}/multi-tenant-cdc/.env"
KAFKA_EXTERNAL_HOST=${KAFKA_EXTERNAL_HOST}
KAFKA_KRAFT_CLUSTER_ID=${KAFKA_KRAFT_CLUSTER_ID}
KAFKA_UI_USER=${KAFKA_UI_USER}
KAFKA_UI_PASSWORD=${KAFKA_UI_PASSWORD}
KAFKA_UI_DOMAIN=${KAFKA_UI_DOMAIN}
DEBEZIUM_UI_DOMAIN=${DEBEZIUM_UI_DOMAIN}

TENANTS=

PGHOST=postgres
PGPORT=5432
PGDATABASE=${POSTGRES_DB}
PGUSER=${POSTGRES_USER}
PGPASSWORD=${POSTGRES_PASSWORD}
PGSSL=false
ENV

FRONTEND_ENV="${ROOT_DIR}/betopia_hrm_frontend-main/.env.example"
API_URL="https://${API_DOMAIN}/api/"
if grep -q '^NEXT_PUBLIC_API_URL=' "${FRONTEND_ENV}"; then
  sed -i "s|^NEXT_PUBLIC_API_URL=.*|NEXT_PUBLIC_API_URL=${API_URL}|" "${FRONTEND_ENV}"
else
  echo "NEXT_PUBLIC_API_URL=${API_URL}" >> "${FRONTEND_ENV}"
fi

if ! grep -q '^NEXT_PUBLIC_MVP_ENABLED=' "${FRONTEND_ENV}"; then
  echo "NEXT_PUBLIC_MVP_ENABLED=true" >> "${FRONTEND_ENV}"
fi

echo "Starting core services..."
cd "${ROOT_DIR}"
docker compose up -d --build

echo "Starting CDC stack..."
cd "${ROOT_DIR}/multi-tenant-cdc"
docker compose up -d --build

echo
echo "Deployment complete. Set DNS A records to ${KAFKA_EXTERNAL_HOST}:"
echo "- ${FRONTEND_DOMAIN}"
echo "- ${API_DOMAIN}"
echo "- ${KAFKA_UI_DOMAIN}"
echo "- ${DEBEZIUM_UI_DOMAIN}"
echo
echo "Once DNS propagates, HTTPS will auto-issue via Let's Encrypt."
