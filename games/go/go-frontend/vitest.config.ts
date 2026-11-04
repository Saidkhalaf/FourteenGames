import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react";

export default defineConfig({
    plugins: [
        react({
            babel: {
                plugins: [["babel-plugin-react-compiler"]],
            },
        }),
    ],

    test: {
        coverage:{
            provider:"v8"
        },
        include: ["test/**/*.test.{ts,tsx}"],
        exclude: ["node_modules", "dist"],
        environment: "jsdom",
        setupFiles: ["./vitest.setup.ts"],
        typecheck: {
            tsconfig: "./tsconfig.app.json"
        },
        globals: true
    }
});
