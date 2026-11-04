import {Stack} from "@mui/material";
import {GoGameBoardPiece} from "./GoGameBoardPiece.tsx";

interface GameBoardPreviewProps {
    size: number;
}

export function GameBoardPreview({size}: GameBoardPreviewProps) {
    const board = Array.from({ length: size }, () =>
        Array.from({ length: size }, () => "_")
    );


    return (
        <Stack direction="row" spacing={0}>
            {board.map((col, colIdx) => (
                <Stack
                    key={"gameboardCol" + colIdx}
                    direction="column"
                    spacing={0}
                    alignItems="center"
                >
                    {col.map((cell, rowIdx) => (
                        <GoGameBoardPiece
                            key={"cell-" + colIdx + "-" + rowIdx}
                            pieceSize={14}
                            stoneVisible={cell !== "_"}
                            atTurn={false}
                            stoneColor={cell === "B" ? "black" : "white"}
                            userColor={"white"}
                            topVisible={rowIdx !== 0}
                            bottomVisible={rowIdx !== size - 1}
                            leftVisible={colIdx !== 0}
                            rightVisible={colIdx !== size - 1}
                        />
                    ))}
                </Stack>
            ))}
        </Stack>
    );
}