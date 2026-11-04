import {Box, Stack, Divider, useMediaQuery} from "@mui/material";
import {Fragment} from "react";
import {GameBoardCell} from "./GameBoardCell.tsx";
import {theme} from "../config/theme/theme.ts";
import type {GameBoard} from "../models/GameBoard.ts";

interface GameBoardProps {
    gameboard: GameBoard
    isInteractive?: boolean
}

export function VisualGameBoard({gameboard,isInteractive=true}: GameBoardProps) {
    const isSmallScreen = useMediaQuery(theme.breakpoints.down("md"));

    return (
        <Box sx={{position: "relative"}}>
            <Stack direction="row"
                   spacing={0}>
                {gameboard.board.map((row, rowIdx) => (
                    <Fragment key={rowIdx}>
                        <Stack direction="column"
                               spacing={0}
                               alignItems="center">
                            {row.map((cell, colIdx) => (
                                <Fragment key={colIdx}>
                                    <GameBoardCell
                                        gameId={gameboard.id}
                                        rowIdx={rowIdx}
                                        columnIdx={colIdx}
                                        isInteractive={isInteractive}
                                        content={cell !== "_" ? cell : ""}
                                        size={isSmallScreen ? 75 : 150}
                                    />
                                    {colIdx < row.length - 1 && (
                                        <Divider orientation="horizontal"
                                                 flexItem
                                                 sx={{borderTopWidth: isSmallScreen ? 7 : 15}}/>
                                    )}
                                </Fragment>
                            ))}
                        </Stack>
                        {rowIdx < gameboard.board.length - 1 &&
                            <Divider orientation="vertical"
                                     sx={{borderRightWidth: isSmallScreen ? 7 : 15}}
                                     flexItem/>
                        }
                    </Fragment>
                ))}
            </Stack>
        </Box>
    );
}