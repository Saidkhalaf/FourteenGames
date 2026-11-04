import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import runtimeEnv from "vite-plugin-runtime-env";

// https://vite.dev/config/
export default defineConfig({
    base: "./",
    plugins: [
        runtimeEnv(
            {
                variableName:'window.env',
                injectHtml:true,
            },
        ),
        react({
            babel: {
                plugins: [['babel-plugin-react-compiler']],
            },
        }),
    ],
    server: {
        port: 5175,
        proxy: {
            '/go/api': {
                target: 'http://localhost:8082',
                changeOrigin: true,
            }
        }
    }
})
