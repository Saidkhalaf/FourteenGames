import {AppBar, Box, Button, Drawer, IconButton, Stack, Toolbar, Typography, useTheme} from "@mui/material";
import {useMediaQueries} from "../hooks/useMediaQueries.tsx";
import MenuIcon from '@mui/icons-material/Menu';
import CloseIcon from '@mui/icons-material/Close';
import {useState} from "react";
import {NotificationStack} from "./NotificationStack.tsx";
import {useSecurityStore} from "../stores/securityStore.ts";
import {NavbarLinks} from "./navbar/NavbarLinks.tsx";

type AppLayoutProps = {
    mainContent?: React.ReactNode;
};

export function AppLayout({mainContent}: AppLayoutProps) {
    const {isSmallScreen} = useMediaQueries();
    const [hamnavOpen, setHamnavOpen] = useState(false);
    const theme = useTheme();
    const login = useSecurityStore((state) => state.login);
    const logout = useSecurityStore((state) => state.logout);
    const isAuthenticated = useSecurityStore((state) => state.isAuthenticated());

    return (
        <>
            <Box sx={{display: "flex", flexDirection: "column", minHeight: "100vh"}}>
                <NotificationStack/>
                <AppBar
                    position="static"
                    sx={{
                        background: theme.palette.primary.main
                    }}
                >
                    <Toolbar sx={{display: "flex", justifyContent: "space-between"}}>
                        <Typography variant="h5"
                                    component="div"
                        >Fourteengames</Typography>
                        <IconButton
                            color="inherit"
                            edge="start"
                            sx={{mr: 2, display: {md: "none"}}}
                            onClick={() => setHamnavOpen(true)}
                        >
                            <MenuIcon/>
                        </IconButton>
                    </Toolbar>
                </AppBar>

                <Drawer anchor="top"
                        open={hamnavOpen}
                        onClose={() => setHamnavOpen(false)}>
                    <Box
                        sx={{position: "relative"}}
                        role="presentation"
                        onClick={() => setHamnavOpen(false)}
                    >
                        <IconButton
                            sx={{position: "absolute", top: 4, right: 4, color: theme.palette.primary.contrastText}}
                            onClick={() => setHamnavOpen(true)}
                        >
                            <CloseIcon/>
                        </IconButton>

                        <Typography variant="h5"
                                    component="div"
                                    sx={{
                                        color: theme.palette.primary.contrastText,
                                        background: theme.palette.primary.main,
                                        p: 2
                                    }}
                        >Fourteengames</Typography>
                        <NavbarLinks isSmallScreen={isSmallScreen}/>
                    </Box>
                </Drawer>

                <Stack
                    direction="row"
                    sx={{
                        flex: 1,
                        backgroundColor: theme.palette.primary.main,
                    }}
                >
                    {!isSmallScreen && (
                        <Stack direction={"column"}
                               sx={{flex: 1}}>
                            <Box
                                sx={{
                                    flex: 1,
                                    m: 2,
                                    backgroundColor: theme.palette.primary.main,
                                }}
                            >
                                <NavbarLinks isSmallScreen={isSmallScreen}/>
                            </Box>


                            <Stack direction={"column"}>
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    sx={{
                                        width: 100,
                                        margin: "0 auto",
                                        mb: 4
                                    }}
                                    onClick={isAuthenticated ? logout : login}>
                                    {isAuthenticated ? "Uitloggen" : "Inloggen"}
                                </Button>
                            </Stack>


                        </Stack>
                    )
                    }
                    {mainContent}
                </Stack>
            </Box>
        </>
    )
}