import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_API = __ENV.BASE_API || 'https://api.betopialtd.com/api';
const KAFKA_UI = __ENV.KAFKA_UI || 'https://kafka.betopialtd.com';
const DEBEZIUM_UI = __ENV.DEBEZIUM_UI || 'https://debezium.betopialtd.com';
const IDENTIFIER = __ENV.IDENTIFIER || 'admin@betopialtd.com';
const PASSWORD = __ENV.PASSWORD || 'Admin@12345';
const DASHBOARD_PATH = __ENV.DASHBOARD_PATH || '/v1/cashAdvanceNotifications?page=1&perPage=1';

export const options = {
  scenarios: {
    api_load: {
      executor: 'ramping-vus',
      exec: 'apiScenario',
      stages: [
        { duration: '2m', target: Number(__ENV.API_VUS || 1000) },
        { duration: '6m', target: Number(__ENV.API_VUS || 1000) },
        { duration: '2m', target: 0 },
      ],
      gracefulRampDown: '30s',
    },
    ui_smoke: {
      executor: 'constant-vus',
      exec: 'uiScenario',
      vus: Number(__ENV.UI_VUS || 50),
      duration: '10m',
    },
    auth_smoke: {
      executor: 'constant-vus',
      exec: 'authScenario',
      vus: Number(__ENV.AUTH_VUS || 10),
      duration: '10m',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<2000'],
  },
};

export function setup() {
  const payload = JSON.stringify({ identifier: IDENTIFIER, password: PASSWORD });
  const res = http.post(`${BASE_API}/v1/auth/login`, payload, {
    headers: { 'Content-Type': 'application/json' },
  });
  if (res.status !== 200) {
    return { token: null };
  }
  try {
    return { token: res.json().token };
  } catch (e) {
    return { token: null };
  }
}

export function apiScenario(data) {
  const token = data.token;
  if (!token) {
    sleep(1);
    return;
  }
  const res = http.get(`${BASE_API}${DASHBOARD_PATH}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  check(res, { 'api 200/403': (r) => r.status === 200 || r.status === 403 });
  sleep(1);
}

export function uiScenario() {
  const kafkaRes = http.get(KAFKA_UI);
  check(kafkaRes, { 'kafka ui 200/302': (r) => r.status === 200 || r.status === 302 });

  const debeziumRes = http.get(DEBEZIUM_UI);
  check(debeziumRes, { 'debezium ui 200/302': (r) => r.status === 200 || r.status === 302 });

  sleep(1);
}

export function authScenario() {
  const payload = JSON.stringify({ identifier: IDENTIFIER, password: PASSWORD });
  const res = http.post(`${BASE_API}/v1/auth/login`, payload, {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'login status 200': (r) => r.status === 200 });
  sleep(1);
}
