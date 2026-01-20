#!/bin/bash

# Root থেকে রান করার জন্য script
# Database config
DB_URL="jdbc:postgresql://51.20.129.254:5432/betopia_hrm"
DB_USER="postgres"
DB_PASS="password"

# Clean command (only for hrm project)
echo "Running Flyway clean on hrm module..."
mvn -pl hrm flyway:clean \
  -Dflyway.url=$DB_URL \
  -Dflyway.user=$DB_USER \
  -Dflyway.password=$DB_PASS \
  -Dflyway.cleanDisabled=false

if [ $? -ne 0 ]; then
  echo "Flyway clean failed. Exiting..."
  exit 1
fi

# Migrate command (only for hrm project)
echo "Running Flyway migrate on hrm module..."
mvn -pl hrm flyway:migrate \
  -Dflyway.url=$DB_URL \
  -Dflyway.user=$DB_USER \
  -Dflyway.password=$DB_PASS

if [ $? -eq 0 ]; then
  echo "Flyway migration completed successfully on hrm module."
else
  echo "Flyway migration failed on hrm module."
  exit 1
fi
