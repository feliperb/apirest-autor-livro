import { defineConfig } from 'vite'

// Development proxy to forward /api requests to the backend
// This avoids CORS while developing locally.
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
