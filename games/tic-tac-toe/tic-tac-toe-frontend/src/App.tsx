import {CssBaseline, ThemeProvider} from "@mui/material";
import {theme} from "./config/theme/theme.ts";
import {GamePage} from "./pages/GamePage.tsx";
import {QueryClientProvider} from "@tanstack/react-query";
import {queryClient} from "./config/api";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";
import {useCurrentPlayerSessionStore} from "./store/gameStore.ts";
import SecurityContextProvider from "./context/SecurityContextProvider.tsx";
import {RouteGuard} from "./components/RouteGuard.tsx";

function App() {
    const updateCurrentPlayerId = useCurrentPlayerSessionStore((state) => state.updateCurrentPlayerId)
    updateCurrentPlayerId("b85182a8-68f8-4d42-b0d5-6166bf2e8284");
    return (
        <>
            <QueryClientProvider client={queryClient}>
                <SecurityContextProvider>
                    <ThemeProvider theme={theme}>
                        <CssBaseline/>
                        <RouteGuard>
                            <GamePage/>
                        </RouteGuard>
                    </ThemeProvider>
                    <ReactQueryDevtools initialIsOpen={false}/>
                </SecurityContextProvider>
            </QueryClientProvider>
        </>
    )
}

export default App
