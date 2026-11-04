import {Stack} from "@mui/material";
import {useParams} from "react-router-dom";
import {GameBoard} from "../components/gameboard/GameBoard.tsx";
import {useGameBoard} from "../hooks/useGameBoard.ts";

export function GamePage() {
    const {id} = useParams();
    const {gameState, isGamePending} = useGameBoard(id!);

    if (isGamePending) {
        return (<div>Loading...</div>)
    }

    return (
        <Stack justifyContent={"center"}
               alignItems={"center"}
               sx={{height: "100svh"}}>
            {gameState && (
                <GameBoard
                    board={gameState}
                />
            )}
        </Stack>
    )
}