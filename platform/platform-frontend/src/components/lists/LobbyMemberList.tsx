import {Button, Stack, Typography, useTheme} from "@mui/material";
import {usePlatformUsers} from "../../hooks/api/user/usePlatformUsers.tsx";
import type {Lobby} from "../../models/lobby/Lobby.ts";
import UserCard from "../cards/UserCard.tsx";
import {JoinLobbyButton} from "../buttons/JoinLobbyButton.tsx";
import {useSecurityStore} from "../../stores/securityStore.ts";
import {useLeaveLobby} from "../../hooks/api/lobby/useLeaveLobby.tsx";
import {useNavigate} from "react-router-dom";


interface LobbyMemberListProps {
    lobby: Lobby;
}

export function LobbyMemberList({lobby}: LobbyMemberListProps) {
    const theme = useTheme();
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const {leaveLobby} = useLeaveLobby();
    const navigate = useNavigate();

    const {userData, isError, isPending} = usePlatformUsers(lobby.players.map(u => u.userId))

    if (isPending) {
        return <div>Loading...</div>
    }

    if (isError || !userData) {
        return <Typography variant={"h4"}
                           sx={{color: theme.palette.primary.main, mt: 2}}>
            Er ging iets mis met het ophalen van de spelerlijst...
        </Typography>
    }
    return (
        <>
            <Stack direction={"row"}
                   gap={4}>
                <Typography variant={"h4"}
                            sx={{color: theme.palette.primary.main, mt: 2}}
                >
                    Spelers in deze lobby
                </Typography>
                {!lobby.players.some(player => player.userId === loggedInUser?.id) ?
                    <JoinLobbyButton lobbyId={lobby.id}/>
                    :
                    <Button
                        variant={"contained"}
                        color={"secondary"}
                        onClick={() => {
                            leaveLobby(lobby.id);
                            navigate("/lobbies");
                        }
                        }
                    >
                        Lobby verlaten
                    </Button>
                }
            </Stack>
            {userData.map(u =>
                <UserCard key={"user-" + u.id}
                          userName={u.userName}
                          isLobbyHost={u.id === lobby.lobbyHostId}/>
            )}
        </>
    )
}