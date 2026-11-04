import {Button, Card, Dialog, DialogActions, DialogTitle, Stack, Typography, useTheme} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useCreateLobby} from "../../hooks/api/lobby/useCreateLobby.tsx";
import {useNotificationStore} from "../../stores/notificationStore.ts";

interface ConfirmationDialogProps {
    isOpen: boolean,
    onClose: () => void,
    gameId:string;
}

export function GameLobbySelectionDialog({isOpen,onClose, gameId}: ConfirmationDialogProps) {
    const theme = useTheme();
    const navigate = useNavigate();
    const addNotification = useNotificationStore((state) => state.addNotification);
    const {addLobbyAsync} = useCreateLobby();

    return (
        <Dialog open={isOpen}
                onClose={onClose}
                maxWidth={"lg"}
        >
            <DialogTitle color={"primary"}
                         variant={"h4"}>
                Lobby
            </DialogTitle>
            <DialogActions>
                <Stack direction={"column"}>
                    <Stack gap={2}
                           direction={"row"}>
                        <Card
                            sx={{
                                maxWidth:300,
                                p: 2,
                                cursor: "pointer"
                            }}
                            onClick={async () => {
                                try{
                                    const lobby = await addLobbyAsync(gameId);
                                    navigate(`/lobby/${lobby.id}`);
                                }catch {
                                    addNotification({
                                        message:"Er ging iets mis bij het maken van de lobby...",
                                        severity:"error"
                                    })
                                }
                            }}
                        >
                            <Typography variant={"h4"}
                                        sx={{color: theme.palette.primary.main}}>
                                Nieuwe lobby openen
                            </Typography>
                            <Typography sx={{color: theme.palette.primary.main}}>
                                Maak een eigen lobby aan en beslis zelf over de configuratie!
                            </Typography>
                        </Card>
                        <Card
                            sx={{
                                maxWidth:300,
                                p: 2,
                                cursor: "pointer"
                            }}
                            onClick={() => {
                                navigate("/lobbies")
                            }}
                        >
                            <Typography variant={"h4"}
                                        sx={{color: theme.palette.primary.main}}>
                                Lobby lijst
                            </Typography>
                            <Typography sx={{color: theme.palette.primary.main}}>
                                Opzoek naar een specifieke lobby? bekijke onze lobby lijst.
                            </Typography>
                        </Card>
                    </Stack>
                    <Button
                        variant={"contained"}
                        color={"secondary"}
                        sx={{mt: 2}}
                        onClick={onClose}
                    >
                        Annuleren
                    </Button>
                </Stack>
            </DialogActions>

        </Dialog>
    )
}
