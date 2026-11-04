import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import runtimeEnv from "vite-plugin-runtime-env";

// https://vite.dev/config/
export default defineConfig({
    plugins: [
        runtimeEnv(
            {
                variableName: 'window.env',
                injectHtml: true,
            },
        ),
        react({
            babel: {
                plugins: [['babel-plugin-react-compiler']],
            },
        }),
    ],
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8070',
                changeOrigin: true,
            }
        }
    }
})
