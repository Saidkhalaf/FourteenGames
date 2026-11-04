import {Box, Button, Stack} from "@mui/material";
import {GoGameBoardPiece} from "./GoGameBoardPiece.tsx";
import type {GameState} from "../../models/GameState.ts";
import {usePlaceStone} from "../../hooks/usePlaceStone.ts";
import {useEffect} from "react";
import {useAiMove} from "../../hooks/useAiMove.ts";
import {usePassTurn} from "../../hooks/usePassTurn.ts";
import {useStartNewAiGame} from "../../hooks/useStartNewAiGame.ts";
import {TurnCard} from "./GameCards/TurnCard.tsx";
import {VictoryCard} from "./GameCards/VictoryCard.tsx";
import {useNavigate} from "react-router-dom";
import {useSecurityStore} from "../../store/securityStore.ts";

interface GameBoardProps {
    board: GameState;
}

export function GameBoard({board}: GameBoardProps) {
    const navigate = useNavigate();
    const loggedInUser = useSecurityStore((state) => state.loggedInUser)
    const {createGame, isPending: isRestarting} = useStartNewAiGame();
    const {requestMove} = usePlaceStone()
    const {requestAiMove, isPending: isAiPending} = useAiMove()
    const {requestPassTurn} = usePassTurn()

    useEffect(() => {
        if (board.isAiGame && !board?.isPlayer1AtTurn && board) {
            console.log("Using the AI player.");
            requestAiMove(board.id);
        }
    }, [board, requestAiMove]);

    const handleRetry = async () => {
        const data = await createGame({size: board.size});
        navigate(`/game/${data.id}`)
    };

    function atTurn() {
        if (board.winner != "EMPTY") return false;
        if (board.isAiGame) {
            return board.isPlayer1AtTurn;
        }
        if (board.isPlayer1AtTurn) {
            return board.player1Id === loggedInUser?.id;
        } else {
            return board.player2Id === loggedInUser?.id;
        }
    }

    return (
        <Stack direction="row"
               spacing={2}
               alignItems="flex-start">
            <Stack direction="row"
                   spacing={0}>
                {board.board.map((col, colIdx) => (
                    <Stack
                        key={"gameboardCol" + colIdx}
                        direction="column"
                        spacing={0}
                        alignItems="center">
                        {col.map((cell, rowIdx) => (
                            <GoGameBoardPiece
                                onClick={() => {
                                    if (atTurn()) {
                                        requestMove({gameId: board.id, moveRequest: {x: colIdx, y: rowIdx}})
                                    }
                                }}
                                key={"cell-" + colIdx + "-" + rowIdx}
                                pieceSize={board.size === 19 ? 50 : 60}
                                stoneVisible={cell != "_"}
                                atTurn={atTurn()}
                                userColor={board.isPlayer1AtTurn ? "white" : "black"}
                                stoneColor={cell === "B" ? "black" : "white"}
                                topVisible={rowIdx != 0}
                                bottomVisible={rowIdx != board.size - 1}
                                leftVisible={colIdx != 0}
                                rightVisible={colIdx !== board.size - 1}
                            />
                        ))}
                    </Stack>
                ))}
            </Stack>

            <Box sx={{minWidth: 260}}>
                <Stack spacing={2}>
                    {board.isAiGame &&
                        <Button variant="outlined"
                                onClick={() => navigate("/")}>
                            Back to Home
                        </Button>
                    }

                    {board.winner.toLowerCase() === "empty" ? (
                        <TurnCard board={board}
                                  atTurn={board.isPlayer1AtTurn}
                                  aiPending={isAiPending}
                                  size={board.size}
                                  id={board.id}
                                  onClick={() => requestPassTurn(board.id)}
                                  canPass={atTurn()}
                                  isLastTurnPassed={board.isLastTurnPassed}/>
                    ) : (
                        <VictoryCard winner={board.winner}
                                     score={board.score}
                                     onClick={handleRetry}
                                     disabled={isRestarting}
                                     isLoading={isRestarting}
                                     isAiGame={board.isAiGame}
                        />
                    )}
                </Stack>
            </Box>
        </Stack>
    )
}