import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
      host: true,
      port: 3000,
      proxy: {
        '/drone-identification-api/drone': {
          target: "http://spring-drone-identification:8080",
          changeOrigin: true,
          secure: false,
          rewrite: (path) => path.replace(/^\/drone-identification-api/, ''),
        },
        '/geo-awareness-api/geozone': {
          target: "http://spring-geo-awareness:8080",
          changeOrigin: true,
          secure: false,
          rewrite: (path) => path.replace(/^\/geo-awareness-api/, ''),
        },
        '/geo-authorization-api/authorization': {
          target: "http://spring-geo-authorization:8080",
          changeOrigin: true,
          secure: false,
          rewrite: (path) => path.replace(/^\/geo-authorization-api/, ''),
        },
        '/weather-api/weather': {
          target: "http://spring-weather:8080",
          changeOrigin: true,
          secure: false,
          rewrite: (path) => path.replace(/^\/weather-api/, ''),
        },
      },
    },
    preview: {
      host: true,
      port: 3000,
    },
  }
);