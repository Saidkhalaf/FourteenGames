import {Button} from "@mui/material";
import axios from "axios";
import {useJoinLobby} from "../../hooks/api/lobby/useJoinLobby.tsx";
import {useNotificationStore} from "../../stores/notificationStore.ts";
import {useNavigate} from "react-router-dom";

interface JoinLobbyButtonProps{
    lobbyId: string;
}

export function JoinLobbyButton({lobbyId}:JoinLobbyButtonProps){
    const {joinLobby, joinLobbyIsError, error} = useJoinLobby();
    const addNotification = useNotificationStore((state) => state.addNotification);
    const navigate = useNavigate();

    if (joinLobbyIsError) {
        if (error && axios.isAxiosError(error)) {
            if(error.response?.status === 409 && error.response?.data?.includes("already inside")){
                addNotification({
                    message:"Je bent al lid van deze lobby...",
                    severity:"error"
                })
            }
        }
        if (error && axios.isAxiosError(error)) {
            if(error.response?.status === 403 && error.response?.data?.includes("is not owned")){
                addNotification({
                    message:"Je bezit deze game niet...",
                    severity:"error"
                })
            }
        }
    }

    return(
        <Button
            variant={"contained"}
            onClick={async () => {
                await joinLobby(lobbyId)
                navigate(`/lobby/${lobbyId}`)
            }}
        >
            Lobby joinen
        </Button>
    )
}