import {theme} from "./config/theme/theme.ts";
import {queryClient} from "./config/api";
import {QueryClientProvider} from "@tanstack/react-query";
import {CssBaseline, ThemeProvider} from '@mui/material';
import {WelcomePage} from "./pages/WelcomePage.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {GamePage} from "./pages/GamePage.tsx";
import {RouteGuard} from "./components/RouteGuard.tsx";
import {FallbackWrapper} from "./components/FallbackWrapper.tsx";
import {ErrorCard} from "./components/ErrorCard.tsx";
import {WelcomePageFallback} from "./pages/fallbacks/WelcomePageFallback.tsx";
import {GamePageLoadingFallback} from "./pages/fallbacks/GamePageLoadingFallback.tsx";
import {basename} from "./config/routing";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";
import {useInitSecurity} from "./hooks/security/useInitSecurity.tsx";

function App() {
    useInitSecurity();
    return (
        <QueryClientProvider client={queryClient}>
            <CssBaseline/>
                <ThemeProvider theme={theme}>
                    <BrowserRouter basename={basename}>
                        <Routes>
                            <Route path={"/"} element={
                                <RouteGuard>
                                    <FallbackWrapper
                                        loadingFallback={<WelcomePageFallback/>}
                                        errorFallback={<ErrorCard title={"Er ging iets mis"}
                                                                  description={"Probeer het nog een keer."}/>}
                                    >
                                        <WelcomePage/>
                                    </FallbackWrapper>
                                </RouteGuard>
                            }/>
                            <Route path={"/game/:id"} element={
                                <RouteGuard>
                                    <FallbackWrapper
                                        loadingFallback={<GamePageLoadingFallback/>}
                                        errorFallback={<ErrorCard title={"Er ging iets mis"}
                                                                  description={"Probeer het nog een keer."}/>}
                                    >
                                        <GamePage/>
                                    </FallbackWrapper>
                                </RouteGuard>

                            }/>
                        </Routes>
                    </BrowserRouter>
                </ThemeProvider>
            <ReactQueryDevtools />
        </QueryClientProvider>
    )
}

export default App
