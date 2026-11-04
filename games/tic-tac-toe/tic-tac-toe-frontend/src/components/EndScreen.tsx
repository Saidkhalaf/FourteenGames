import {Button, Stack, Typography} from "@mui/material";
import type {GameStatus} from "../models/GameStatus.ts";
import {VisualGameBoard} from "./VisualGameBoard.tsx";
import type {GameBoard} from "../models/GameBoard.ts";

interface EndScreenProps {
    gameBoard:GameBoard,
    winningUser: string;
    status: GameStatus;
    createGame: () => void;
}

export function EndScreen({gameBoard,winningUser,status,createGame}: EndScreenProps) {

    return (
        <>
            <Stack direction={"row"}>
                <Stack
                    direction={"column"}
                    alignItems={"center"}
                    justifyContent={"center"}
                    sx={{
                        p:4
                }}
                >
                    <Typography variant={"h2"} sx={{m:2}}>
                       Eindstatus spelbord
                    </Typography>
                    <VisualGameBoard isInteractive={false} gameboard={gameBoard}/>
                </Stack>
                <Stack
                    direction={"column"}
                    alignItems={"center"}
                    justifyContent={"center"}
                    sx={{
                        p: 4,
                        height:"100svh"
                    }}
                >
                    {status === "WON" &&
                        <Typography variant={"h3"}>{winningUser} heeft gewonnen!</Typography>
                    }
                    {status === "DRAW" &&
                        <Typography variant={"h3"}>Gelijkspel!</Typography>
                    }
                    <Button variant={"contained"}
                            sx={{m: 2}}
                            onClick={createGame}
                    >
                        Nieuw spel starten
                    </Button>
                </Stack>
            </Stack>
        </>
    )
}