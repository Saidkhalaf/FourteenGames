import {Typography, Stack, Avatar, Button, Card, useTheme} from "@mui/material";
import {Navigate, Link} from "react-router-dom";
import {ThemeControls} from "../components/controls/ThemeControls.tsx";
import {useSecurityStore} from "../stores/securityStore.ts";
import {usePlatformUser} from "../hooks/usePlatformUser.tsx";

export function UserPreferencePage() {
    const isAuthenticated = useSecurityStore((state) => state.isAuthenticated);
    const isInitialised = useSecurityStore((state) => state.isInitialised);
    const login = useSecurityStore((state) => state.login);
    const manageAccount = useSecurityStore((state) => state.manageAccount);
    const logout = useSecurityStore((state) => state.logout);
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const {platformUser} = usePlatformUser();

    const theme = useTheme();
    if (isInitialised && !isAuthenticated()) {
        return <Navigate to="/games"
                         replace/>
    }

    return (
        <>
            <Card sx={{maxWidth: "100%", mx: 'auto', mt: 5, borderRadius: 3,p:4, overflow: 'hidden', color:theme.palette.primary.main}}>
                <Stack direction="row"
                       justifyContent="flex-end">
                    <Button
                        sx={{
                            mr:2
                        }}
                        color={"primary"}
                        variant={"contained"}
                        onClick={manageAccount}
                    >
                        inloggegevens wijzigen
                    </Button>
                    <Button sx={{width: "25%"}}
                            variant="contained"
                            color="secondary"
                            onClick={isAuthenticated() ? logout : login}>
                        {isAuthenticated() ? "Uitloggen" : "Inloggen"}
                    </Button>
                </Stack>

                <Stack
                    direction={{xs: "column", sm: "column", md: "row"}}
                    sx={{
                        mt: 2,
                        minHeight: "50%"
                    }}
                >
                    <Stack
                        direction={"column"}
                        sx={{
                            flex: 1,
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center"
                        }}
                    >
                        <Avatar
                            src={platformUser.profilePictureUrl || undefined}
                            sx={{
                                width: 150,
                                height: 150,
                                margin:"0 auto",
                                border: '3px solid white',
                                bgcolor: platformUser.profilePictureUrl ? 'transparent' : theme.palette.primary.main,
                                color: theme.palette.primary.contrastText,
                            }}
                        />
                        <Typography
                            variant={"h4"}
                            sx={{mt: 2}}
                        >
                            {loggedInUser?.username || "Gebruiker"}
                        </Typography>
                        <Button
                            component={Link}
                            to={`/profile/${loggedInUser?.username}`}
                        >
                            Profiel bekijken
                        </Button>
                    </Stack>
                    <Stack direction={"column"}
                           sx={{
                               flex: 1,
                               p: 2
                           }}
                    >
                        <Typography
                            variant={"h4"}
                            sx={{
                                width: "100%"
                            }}
                        >
                            Uw gegevens
                        </Typography>
                        {loggedInUser && (
                            <Stack direction={"column"}
                                   sx={{mt: 2}}>
                                {(loggedInUser.firstName || loggedInUser.lastName) && (
                                    <Typography>
                                        Naam: {[loggedInUser.firstName, loggedInUser.lastName].filter(Boolean).join(" ")}
                                    </Typography>
                                )}
                                {loggedInUser.email && (
                                    <Typography>Email: {loggedInUser.email}</Typography>
                                )}
                            </Stack>
                        )}
                        <Typography
                            variant={"h4"}
                            sx={{
                                width: "100%",
                                mt:2
                            }}
                        >
                            Uw voorkeuren
                        </Typography>
                        <Stack direction={"row"}
                               sx={{
                                   display: "flex",
                                   alignItems: "center",
                                   justifyContent: "space-between",
                               }}>
                            <Typography sx={{mr: 2}}>Thema:</Typography>
                            <ThemeControls/>
                        </Stack>
                    </Stack>

                </Stack>
            </Card>
        </>
    )
}