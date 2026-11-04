import {Button, Card, Dialog, DialogActions, DialogTitle, Stack, Typography, useTheme} from "@mui/material";
import {useSelectionStore} from "../../stores/selectionStore.ts";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {GameLobbySelectionDialog} from "./GameLobbySelectionDialog.tsx";
import {useStartTrainingGame} from "../../hooks/api/lobby/useStartTrainingGame.tsx";

interface ConfirmationDialogProps {
    isOpen: boolean,
    onClose: () => void,
}

export function GameModeSelectionDialog({isOpen, onClose}: ConfirmationDialogProps) {
    const theme = useTheme();
    const navigate = useNavigate();
    const selectedGame = useSelectionStore((state) => state.selectedGameId);
    const [isSelectingLobbyMethod, setIsSelectingLobbyMethod] = useState(false);
    const {startTraining} = useStartTrainingGame();

    return (
        <>
            <Dialog open={isOpen}
                    onClose={onClose}>
                <DialogTitle color={"primary"}
                             variant={"h4"}>
                    Spelmodus
                </DialogTitle>
                <DialogActions>
                    <Stack direction={"column"}>
                        <Stack gap={2}
                               direction={"row"}>
                            <Card
                                sx={{
                                    p: 2,
                                    cursor: "pointer"
                                }}
                                onClick={async () => {
                                    if (selectedGame) {
                                        await startTraining(selectedGame);
                                    }
                                    navigate(`/games/${selectedGame}`)
                                }}
                            >
                                <Typography variant={"h4"}
                                            sx={{color: theme.palette.primary.main}}>
                                    Training
                                </Typography>
                                <Typography sx={{color: theme.palette.primary.main}}>
                                    Verbeter je skills en train tegen AI spelers.
                                </Typography>
                            </Card>
                            <Card
                                sx={{
                                    p: 2,
                                    cursor: "pointer"
                                }}
                                onClick={() => {
                                    setIsSelectingLobbyMethod(true);
                                }}
                            >
                                <Typography variant={"h4"}
                                            sx={{color: theme.palette.primary.main}}>
                                    Online
                                </Typography>
                                <Typography sx={{color: theme.palette.primary.main}}>
                                    Bewijs je skills en speel online tegen andere spelers!
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
            <GameLobbySelectionDialog isOpen={isSelectingLobbyMethod}  gameId={selectedGame ?? ""} onClose={() => setIsSelectingLobbyMethod(false)}/>
        </>
    )
}
