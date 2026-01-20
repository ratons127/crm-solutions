import axios, { AxiosError, AxiosInstance } from 'axios';
import { decryptJSONFromLocalStorage } from '@/lib/utils/secureStorage';

// Create a new Axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL + 'v1',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/hal+json',
  },
});

// ---- Request Interceptor ----
apiClient.interceptors.request.use(
  async config => {
    // Read encrypted token fresh on every request
    let token: string | null = null;
    if (typeof window !== 'undefined') {
      try {
        token = await decryptJSONFromLocalStorage<string>('token');
        if (!token) {
          // Backward-compat: try plaintext if any (migration)
          token = localStorage.getItem('token');
        }
      } catch {
        // ignore
      }
    }

    const headers: any = config.headers ?? {};

    if (token) {
      headers.Authorization = `Bearer ${token}`;
    } else if (headers.Authorization) {
      delete headers.Authorization;
    }

    config.headers = headers;
    return config;
  },
  (error: AxiosError) => Promise.reject(error)
);

// ---- Response Interceptor ----
apiClient.interceptors.response.use(
  response => response,
  (error: AxiosError) => Promise.reject(error)
);

export default apiClient;

