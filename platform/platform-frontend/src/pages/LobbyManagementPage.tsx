import {Box, Card, Typography, useTheme} from "@mui/material";
import {useLobby} from "../hooks/api/lobby/useLobby.tsx";
import {useNavigate, useParams} from "react-router-dom";
import dayjs from "dayjs";
import {LobbyMemberList} from "../components/lists/LobbyMemberList.tsx";
import {ConfirmationDialog} from "../components/dialogs/ConfirmationDialog.tsx";
import {useState} from "react";
import {LobbyGameSettings} from "../components/LobbyGameSettings.tsx";

export function LobbyManagementPage() {
    const {lobbyId} = useParams();
    const {lobby} = useLobby(lobbyId!);
    const theme = useTheme();
    const [closedStartMessage, setClosedStartMessage] = useState(false);
    const navigate = useNavigate();

    function navigateToGame() {
        navigate(`/games/${lobby.gameId}/${lobby.currentGameSessionId}`)
    }

    return (
        <>
            <Card
                sx={{
                    maxWidth: "100%",
                    p: 2,
                    mx: "auto",
                    mt: 5,
                    overflow: "hidden",
                    display: "flex",
                    minHeight: 250,
                }}
            >
                <Box
                    sx={{
                        flex: 1,
                    }}
                >
                    <Typography variant={"h2"}
                                sx={{color: theme.palette.primary.main}}>
                        Lobby beheer
                    </Typography>
                    <Typography sx={{color: theme.palette.primary.main}}>
                        Lobbyid: {lobby.id}
                    </Typography>
                    <Typography sx={{color: theme.palette.primary.main}}>
                        De lobby werd geopend op {dayjs(lobby.creationDate).format('HH:mm DD/MM/YYYY')}
                    </Typography>
                    <Typography sx={{color: theme.palette.primary.main}}
                                fontWeight={"bold"}>
                        {lobby.players.length}/{lobby.maxPlayers} spelers in de lobby
                    </Typography>
                    <LobbyMemberList lobby={lobby}/>
                </Box>

                <Box
                    sx={{
                        flex: 1,
                        borderRadius: 0,
                    }}
                >
                    <LobbyGameSettings gameId={lobby.gameId} lobby={lobby}/>
                </Box>
            </Card>
            <ConfirmationDialog
                acceptButtonContent={"Spelen"}
                rejectButtonContent={"Nog even de lobby bekijken"}
                isOpen={lobby.currentGameSessionId != null && !closedStartMessage}
                confirmationMessage={"De lobby host heeft het spel gestart, klaar om te spelen?"}
                onAccept={() => {
                    navigateToGame()
                }}
                onClose={() => {
                    setClosedStartMessage(true)
                }}
            />
        </>
    )
}