import {BrowserRouter} from "react-router-dom";
import {CssBaseline, ThemeProvider} from "@mui/material";
import {theme} from "./config/theme/theme.ts";
import {MainContentContainer} from "./components/MainContentContainer.tsx";
import {AppLayout} from "./components/AppLayout.tsx";
import {QueryClientProvider} from "@tanstack/react-query";
import {queryClient} from "./config/api";
import {useInitSecurity} from "./hooks/security/useInitSecurity.tsx";
import {ReactRouterConfig} from "./components/config/ReactRouterConfig.tsx";
import {ChatBox} from "./components/chatbot/ChatBox.tsx";

function App() {
    useInitSecurity();

    return (
        <>
            <QueryClientProvider client={queryClient}>
                <ThemeProvider theme={theme}>
                    <CssBaseline/>
                    <BrowserRouter>
                        <AppLayout mainContent={
                            <MainContentContainer>
                                <ReactRouterConfig/>
                            </MainContentContainer>
                        }/>
                    </BrowserRouter>
                    <ChatBox/>
                </ThemeProvider>
            </QueryClientProvider>
        </>
    )
}

export default App
