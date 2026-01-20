'use client';

// Lightweight AES-GCM helper to encrypt/decrypt JSON into localStorage
// Key management: prefer a non-extractable, in-memory key per page lifecycle.
// Optionally, a persistent, non-extractable CryptoKey can be stored in
// IndexedDB (opt-in) for decryption across reloads. If no persistent key is
// available, callers should re-fetch fresh data from the API.

export type EncryptedEnvelope = {
  v: number; // version
  alg: 'AES-GCM';
  iv: string; // base64
  ct: string; // base64 ciphertext
  exp?: number; // optional expiration epoch ms
  createdAt?: number; // epoch ms
};

// Back-compat name used previously for sessionStorage. We no longer store
// raw keys there, but we keep the constant to allow cleanup if needed.
const SESSION_KEY_NAME = 'ss:enc:key:v1';
const ENV_KEY_B64 = process.env.SECURE_STORAGE_KEY_B64;
const ENV_KEY_TEXT = process.env.SECURE_STORAGE_KEY;

const te =
  typeof TextEncoder !== 'undefined' ? new TextEncoder() : (undefined as any);
const td =
  typeof TextDecoder !== 'undefined' ? new TextDecoder() : (undefined as any);

function toB64(buf: ArrayBuffer | Uint8Array): string {
  const u8 = buf instanceof Uint8Array ? buf : new Uint8Array(buf);
  let s = '';
  for (let i = 0; i < u8.length; i++) s += String.fromCharCode(u8[i]);
  return btoa(s);
}

function fromB64(b64: string): ArrayBuffer {
  const bin = atob(b64);
  const u8 = new Uint8Array(bin.length);
  for (let i = 0; i < bin.length; i++) u8[i] = bin.charCodeAt(i);
  return u8.buffer;
}

async function deriveKeyFromText(
  passphrase: string
): Promise<CryptoKey | null> {
  if (!te) return null;
  const data = te.encode(passphrase);
  const hash = await crypto.subtle.digest('SHA-256', data);
  return crypto.subtle.importKey('raw', hash, { name: 'AES-GCM' }, false, [
    'encrypt',
    'decrypt',
  ]);
}

function tryDecodeBase64Key(b64?: string): ArrayBuffer | null {
  if (!b64) return null;
  try {
    const buf = fromB64(b64);
    const len = new Uint8Array(buf).length;
    if (len === 16 || len === 24 || len === 32) return buf; // AES-128/192/256
    return null;
  } catch {
    return null;
  }
}

async function getStaticKeyFromEnv(): Promise<CryptoKey | null> {
  if (typeof window === 'undefined' || !window.crypto?.subtle) return null;
  try {
    const raw = tryDecodeBase64Key(ENV_KEY_B64);
    if (raw) {
      return await crypto.subtle.importKey(
        'raw',
        raw,
        { name: 'AES-GCM' },
        false,
        ['encrypt', 'decrypt']
      );
    }
    if (ENV_KEY_TEXT) {
      return await deriveKeyFromText(ENV_KEY_TEXT);
    }
    return null;
  } catch {
    return null;
  }
}

// Optional IndexedDB persistence for the non-extractable CryptoKey
const IDB_DB_NAME = 'secure-storage';
const IDB_STORE_NAME = 'keys';
const IDB_KEY_ID = 'aes-gcm:v1';
let ENABLE_IDB_PERSISTENCE = false;

let inMemoryKey: CryptoKey | null = null;
let inFlightKeyPromise: Promise<CryptoKey | null> | null = null;

function openKeyDB(): Promise<IDBDatabase> {
  return new Promise((resolve, reject) => {
    try {
      const req = indexedDB.open(IDB_DB_NAME, 1);
      req.onupgradeneeded = () => {
        try {
          const db = req.result;
          if (!db.objectStoreNames.contains(IDB_STORE_NAME)) {
            db.createObjectStore(IDB_STORE_NAME);
          }
        } catch (e) {
          // ignore
        }
      };
      req.onsuccess = () => resolve(req.result);
      req.onerror = () => reject(req.error);
    } catch (e) {
      reject(e);
    }
  });
}

async function idbGetKey(): Promise<CryptoKey | null> {
  if (!ENABLE_IDB_PERSISTENCE) return null;
  try {
    const db = await openKeyDB();
    return await new Promise((resolve, reject) => {
      const tx = db.transaction(IDB_STORE_NAME, 'readonly');
      const store = tx.objectStore(IDB_STORE_NAME);
      const req = store.get(IDB_KEY_ID);
      req.onsuccess = () => resolve((req.result as CryptoKey) || null);
      req.onerror = () => reject(req.error);
    });
  } catch {
    return null;
  }
}

async function idbPutKey(key: CryptoKey): Promise<void> {
  if (!ENABLE_IDB_PERSISTENCE) return;
  try {
    const db = await openKeyDB();
    await new Promise<void>((resolve, reject) => {
      const tx = db.transaction(IDB_STORE_NAME, 'readwrite');
      const store = tx.objectStore(IDB_STORE_NAME);
      const req = store.put(key, IDB_KEY_ID);
      req.onsuccess = () => resolve();
      req.onerror = () => reject(req.error);
    });
  } catch {
    // ignore
  }
}

async function idbDeleteKey(): Promise<void> {
  try {
    const db = await openKeyDB();
    await new Promise<void>((resolve, reject) => {
      const tx = db.transaction(IDB_STORE_NAME, 'readwrite');
      const store = tx.objectStore(IDB_STORE_NAME);
      const req = store.delete(IDB_KEY_ID);
      req.onsuccess = () => resolve();
      req.onerror = () => reject(req.error);
    });
  } catch {
    // ignore
  }
}

async function ensureInMemoryKey(): Promise<CryptoKey | null> {
  if (typeof window === 'undefined' || !window.crypto?.subtle) return null;

  if (inMemoryKey) return inMemoryKey;
  if (inFlightKeyPromise) return inFlightKeyPromise;

  inFlightKeyPromise = (async () => {
    // Try loading from IndexedDB if enabled
    const persisted = await idbGetKey();
    if (persisted) {
      inMemoryKey = persisted;
      return inMemoryKey;
    }

    try {
      const key = await crypto.subtle.generateKey(
        { name: 'AES-GCM', length: 256 },
        false, // non-extractable
        ['encrypt', 'decrypt']
      );
      inMemoryKey = key;
      await idbPutKey(key);
      return key;
    } catch {
      return null;
    }
  })();

  const k = await inFlightKeyPromise;
  inFlightKeyPromise = null;
  return k;
}

async function ensureCryptoKey(): Promise<CryptoKey | null> {
  // Prefer static key from env (cross-tab, persistent), otherwise fall back to
  // a non-extractable, per-lifecycle key (optionally persisted in IndexedDB).
  const staticKey = await getStaticKeyFromEnv();
  if (staticKey) return staticKey;
  return ensureInMemoryKey();
}

export function clearSessionEncryptionKey(): void {
  if (typeof window === 'undefined') return;
  try {
    // Best-effort cleanup of legacy storage and current in-memory/IDB keys
    try {
      sessionStorage.removeItem(SESSION_KEY_NAME);
    } catch {
      // ignore
    }
    inMemoryKey = null;
    void idbDeleteKey();
  } catch {
    // ignore
  }
}

// Opt-in to persisting the non-extractable CryptoKey in IndexedDB.
// Must be called early in app startup (before first encrypt/decrypt) if desired.
export function enableIndexedDBKeyPersistence(enable = true): void {
  ENABLE_IDB_PERSISTENCE = !!enable;
}

export async function encryptJSONToLocalStorage(
  storageKey: string,
  value: any,
  opts?: { ttlMs?: number }
): Promise<boolean> {
  if (typeof window === 'undefined' || !window.crypto?.subtle || !te)
    return false;
  try {
    const key = await ensureCryptoKey();
    if (!key) return false;
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const plaintext = te.encode(JSON.stringify(value));
    const ctBuf = await crypto.subtle.encrypt(
      { name: 'AES-GCM', iv },
      key,
      plaintext
    );
    const envelope: EncryptedEnvelope = {
      v: 1,
      alg: 'AES-GCM',
      iv: toB64(iv),
      ct: toB64(ctBuf),
      createdAt: Date.now(),
    };
    if (opts?.ttlMs && opts.ttlMs > 0) envelope.exp = Date.now() + opts.ttlMs;
    localStorage.setItem(storageKey, JSON.stringify(envelope));
    return true;
  } catch {
    return false;
  }
}

export async function decryptJSONFromLocalStorage<T = any>(
  storageKey: string
): Promise<T | null> {
  if (typeof window === 'undefined' || !window.crypto?.subtle || !td)
    return null;
  try {
    const raw = localStorage.getItem(storageKey);
    if (!raw) return null;

    // Try to parse as our envelope; if plain JSON, bail to caller to handle.
    let env: EncryptedEnvelope | null = null;
    try {
      env = JSON.parse(raw);
    } catch {
      return null;
    }
    if (!env || env.alg !== 'AES-GCM' || !env.iv || !env.ct) return null;
    if (env.exp && Date.now() > env.exp) {
      localStorage.removeItem(storageKey);
      return null;
    }

    const key = await ensureCryptoKey();
    if (!key) return null;

    const ptBuf = await crypto.subtle.decrypt(
      { name: 'AES-GCM', iv: new Uint8Array(fromB64(env.iv)) },
      key,
      fromB64(env.ct)
    );
    const json = td.decode(ptBuf);
    return JSON.parse(json) as T;
  } catch {
    return null;
  }
}

export function clearLocalStorageItem(storageKey: string): void {
  if (typeof window === 'undefined') return;
  try {
    localStorage.removeItem(storageKey);
  } catch {
    // ignore
  }
}
