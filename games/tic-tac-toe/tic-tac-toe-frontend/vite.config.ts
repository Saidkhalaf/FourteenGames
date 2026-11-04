import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
    base: "/gamehosts/tic-tac-toe/",
    plugins: [
        react({
            babel: {
                plugins: [['babel-plugin-react-compiler']],
            },
        }),
    ],
    server: {
        port: 5174,
        proxy: {
            '/tic-tac-toe/api': {
                target: 'http://localhost:8081',
                changeOrigin: true,
            }
        }
    }
})
