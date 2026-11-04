import {Alert, Box, Snackbar, Typography, useTheme} from "@mui/material";
import {useNewGameMove} from "../hooks/useNewGameMove.ts";
import type {MoveRequest} from "../models/MoveRequest.ts";
import {useState} from "react";


interface GameBoardCellProps {
    rowIdx: number;
    gameId: string;
    columnIdx: number;
    content: string;
    size?: number;
    isInteractive?:boolean;
}

export function GameBoardCell({gameId, rowIdx, columnIdx, content, size = 50,isInteractive=true}: GameBoardCellProps) {
    const theme = useTheme();
    const {requestMove} = useNewGameMove();
    const [notifyWrongPlacement, setNotifyWrongPlacement] = useState(false);

    function handleCellSelection() {
        if(!isInteractive){return;}
        if (content != "") {
            setNotifyWrongPlacement(true);
            return;
        }
        const move: MoveRequest = {
            y: columnIdx,
            x: rowIdx,
            player: "b85182a8-68f8-4d42-b0d5-6166bf2e8284"
        };
        requestMove({gameId: gameId, moveRequest: move});
    }


    return (
        <>
            <Box sx={{
                width: size,
                height: size,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
            }}
                 onClick={handleCellSelection}
            >
                <Typography
                    fontWeight={"bold"}
                    fontSize={size}
                    sx={{
                        color: content === "X" ? theme.palette.primary.contrastText : theme.palette.secondary.main,
                    }}
                >
                    {content}
                </Typography>
            </Box>
            <Snackbar
                open={notifyWrongPlacement}
                autoHideDuration={3000}
                onClose={() => setNotifyWrongPlacement(false)}
                anchorOrigin={{vertical: "top", horizontal: "center"}}
            >
                <Alert severity="warning">
                    <Typography>
                        Dit vakje werd al gespeeld...
                    </Typography>
                </Alert>
            </Snackbar>
        </>
    )
}
