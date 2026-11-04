import {Button, MenuItem, Select, type SelectChangeEvent, Typography, useTheme} from "@mui/material";
import {useGame} from "../hooks/api/games/useGame.tsx";
import {useEffect, useState} from "react";
import type {Lobby} from "../models/lobby/Lobby.ts";
import {useLobbyStartGame} from "../hooks/api/lobby/useLobbyStartGame.tsx";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {useNotificationStore} from "../stores/notificationStore.ts";
import {useSecurityStore} from "../stores/securityStore.ts";

interface LobbyGameSettingsProps {
    gameId: string;
    lobby: Lobby
}

export function LobbyGameSettings({gameId, lobby}: LobbyGameSettingsProps) {
    const theme = useTheme();
    const {game} = useGame(gameId);
    const {startGame, startGameError, error} = useLobbyStartGame();
    const addNotification = useNotificationStore((state) => state.addNotification);
    const navigate = useNavigate();
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const [selectedSettings, setSelectedSettings] =
        useState<Record<string, unknown>>({});

    function navigateToGame() {
        navigate(`/games/${lobby.gameId}`)
    }

    useEffect(() => {
        if (!game?.configurableSettings) return;
        const initState: Record<string, unknown> = {};
        Object.entries(game.configurableSettings).forEach(([key, value]) => {
            if (Array.isArray(value) && value.length > 0) {
                initState[key] = value[0];
            }
        });
        setSelectedSettings(initState);
    }, [game]);


    if (startGameError) {
        if (error && axios.isAxiosError(error)) {
            if (error.response?.status === 403 && error.response?.data?.includes("Non lobby manager")) {
                addNotification({
                    message: "Je hebt geen rechten om een spel te starten voor deze lobby...",
                    severity: "error"
                })
            }
            if (error.response?.status === 409 && error.response?.data?.includes("Lobby is not full yet")) {
                addNotification({
                    message: "Deze lobby mist nog spelers...",
                    severity: "warning"
                })
            }
        }
    }


    const handleChange =
        (key: string) =>
            (event: SelectChangeEvent<unknown>) => {
                setSelectedSettings(prev => ({
                    ...prev,
                    [key]: event.target.value,
                }));
            };

    return (
        <>
            {lobby.lobbyHostId === loggedInUser?.id &&Object.keys(game.configurableSettings).length != 0 &&
                <>
                    <Typography
                        variant="h4"
                        sx={{color: theme.palette.primary.main, mt: 2}}
                    >
                        Instellingen
                    </Typography>

                    {Object.entries(game.configurableSettings).map(([key, value]) => {
                        if (!Array.isArray(value) || value.length === 0) return null;

                        return (
                            <div key={key}>
                                <Typography
                                    variant="h6"
                                    sx={{color: theme.palette.primary.main}}
                                >
                                    {key.toLowerCase()}
                                </Typography>

                                <Select
                                    sx={{mb: 2}}
                                    fullWidth
                                    value={selectedSettings[key] ?? value[0]}
                                    onChange={handleChange(key)}
                                >
                                    {value.map((option, index) => (
                                        <MenuItem key={index}
                                                  value={option}>
                                            {String(option)}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </div>
                        );
                    })}
                </>
            }
            {lobby.currentGameSessionId === null ?
                <>
                    {lobby.lobbyHostId === loggedInUser?.id &&
                        <Button
                            color={"secondary"}
                            sx={{
                                mt: 2,
                            }}
                            variant={"contained"}
                            onClick={() => {
                                if(lobby.players.length < lobby.maxPlayers){
                                    addNotification({message:"Deze lobby is nog niet vol.", severity:"warning"})
                                    return
                                }
                                startGame({lobby: lobby, settings: selectedSettings});
                            }}
                        >
                            Spel starten
                        </Button>
                    }
                </>
                :
                <>
                    <Typography sx={{color: theme.palette.primary.main, mt: 2}}
                                fontWeight={"bold"}
                                variant={"h4"}>
                        Het spel is reeds gestart!
                    </Typography>
                    <Button
                        color={"primary"}
                        sx={{mt: 1}}
                        variant={"contained"}
                        onClick={() => {
                            navigateToGame();
                        }}
                    >
                        Naar het spel
                    </Button>
                </>
            }
        </>
    )
        ;
}
