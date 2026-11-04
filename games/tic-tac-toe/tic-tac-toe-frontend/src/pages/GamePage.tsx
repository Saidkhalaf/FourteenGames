import {type MouseEvent, useEffect, useState} from "react";
import {Typography, Box, Stack, Button, Paper, useTheme, Snackbar, Alert} from "@mui/material";
import {useStartNewGame} from "../hooks/useStartNewGame.ts";
import {VisualGameBoard} from "../components/VisualGameBoard.tsx";
import {CurrentPlayerComponent} from "../components/CurrentPlayerComponent.tsx";
import {useAiMove} from "../hooks/useAiMove.ts";
import {useGameBoard} from "../hooks/useGameBoard.ts";
import {EndScreen} from "../components/EndScreen.tsx";
import {useCurrentPlayerSessionStore} from "../store/gameStore.ts";


export function GamePage() {
    const {createGame, isPending} = useStartNewGame();
    const {gameState} = useGameBoard();
    const {requestAiMove} = useAiMove();
    const currentPlayerId = useCurrentPlayerSessionStore((state) => state.currentPlayerId)
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const theme = useTheme();

    useEffect(() => {
        if (!gameState?.atTurn && gameState && gameState.status === "IN_PROGRESS") {
            console.log("Using the AI player.");
            requestAiMove(gameState.id);
        }
    }, [gameState?.atTurn, gameState?.status]);


    if (isPending) {
        return (<Stack
            direction={"column"}
            alignItems={"center"}
            justifyContent={"center"}
            sx={{
                p: 4,
                height: "100svh"
            }}
        >
            <Typography variant={"h4"}>Game starting...</Typography>
        </Stack>)
    }

    if(currentPlayerId === null){
        return (<Stack
            direction={"column"}
            alignItems={"center"}
            justifyContent={"center"}
            sx={{
                p: 4,
                height: "100svh"
            }}
        >
            <Typography variant={"h4"}>Player not signed in... Logout and try again!</Typography>
        </Stack>)
    }

    function handleMoveWhileNotAtTurn(e: MouseEvent) {
        e.stopPropagation();
        setSnackbarOpen(true);
    }

    if (gameState?.status === "WON" || gameState?.status === "DRAW") {
        return <EndScreen
            gameBoard={gameState}
            createGame={() => createGame({player: currentPlayerId})}
            winningUser={gameState.winner}
            status={gameState.status}/>;
    }

    return (
        <>
            <Box
                sx={{p: 2, position: "relative"}}
            >
                {gameState &&
                    <Paper
                        className={"overlay"}
                        sx={{
                            zIndex: 100,
                            position: "absolute",
                            height: "100vh",
                            top: 0,
                            opacity: 0.3,
                            left: 0,
                            right: 0,
                            bottom: 0,
                            display: gameState.atTurn ? "none" : "initial",
                            background: theme.palette.primary.main,
                        }}
                        onClick={(e) => {
                            handleMoveWhileNotAtTurn(e)
                        }}
                    />
                }
                <Stack direction={"row"}>
                    <Typography variant={"h4"}>Tic Tac Toe</Typography>
                    {!gameState &&
                        <Button variant={"contained"}
                                sx={{ml: 2}}
                                onClick={() => createGame({player: currentPlayerId})}>
                            (DEV_BUTTON) Start a game
                        </Button>}
                </Stack>
                {gameState &&
                    <>
                        <CurrentPlayerComponent isCurrentlyPlaying={gameState.atTurn}
                                                currentUser={gameState.atTurn ? gameState.player1 : gameState.player2}/>
                        <Stack sx={{width: "100%", display: "flex", alignItems: "center", justifyContent: "center"}}>
                            <VisualGameBoard gameboard={gameState}/>
                        </Stack>
                    </>
                }
            </Box>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                onClose={() => setSnackbarOpen(false)}
                anchorOrigin={{vertical: "top", horizontal: "center"}}
            >
                <Alert severity="warning"
                       onClose={() => setSnackbarOpen(false)}>
                    Het is niet jouw beurt :'(
                </Alert>
            </Snackbar>
        </>
    );
}