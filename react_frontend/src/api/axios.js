import axios from 'axios';

// Use relative `/api` baseURL so Vite dev server proxy (vite.config.js)
// can forward requests to the backend at http://localhost:8080.
// In production this should point to the actual backend URL.
const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
});

export default api;
