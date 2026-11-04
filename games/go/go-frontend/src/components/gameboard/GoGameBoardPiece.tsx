import {Box, useTheme} from "@mui/material";
import {useState} from "react";

interface GoGameBoardPieceProps {
    pieceSize?: number;
    stoneVisible: boolean;
    stoneColor?: string;
    userColor:string;
    atTurn: boolean;
    topVisible?: boolean;
    bottomVisible?: boolean;
    leftVisible?: boolean;
    rightVisible?: boolean;
    onClick?: () => void;
}

export function GoGameBoardPiece(
    {
        pieceSize = 60,
        stoneVisible,
        stoneColor,
        atTurn,
        topVisible = true,
        bottomVisible = true,
        leftVisible = true,
        rightVisible = true,
        onClick = () => {},
        userColor
    }: GoGameBoardPieceProps) {
    const theme = useTheme();
    const [hovering, setHovering] = useState(false)
    return (
        <Box
            data-testid="go-game-board-piece"
            onClick={onClick}
            sx={{
                position: "relative",
                width: pieceSize,
                height: pieceSize,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                aspectRatio: "1/1",
                cursor: 'pointer'
            }}
            onMouseEnter={() => setHovering(true)}
            onMouseLeave={() => setHovering(false)}
        >
            {((hovering && atTurn) || stoneVisible) && (
                <Box
                    sx={{
                        width: pieceSize * 0.9,
                        height: pieceSize * 0.9,
                        borderRadius: "50%",
                        backgroundColor: (stoneVisible ? stoneColor : userColor),
                        border: "5px solid " + theme.palette.primary.main,
                        zIndex: 1,
                        opacity: (stoneVisible ? 1 : 0.7)
                    }}
                />
            )}

            {topVisible && (
                <Box
                    sx={{
                        position: "absolute",
                        width: pieceSize / 12,
                        height: "50%",
                        backgroundColor: "black",
                        top: 0,
                        left: "50%",
                        transform: "translateX(-50%)",
                        zIndex: 0,
                    }}
                />
            )}

            {bottomVisible && (
                <Box
                    sx={{
                        position: "absolute",
                        width: pieceSize / 12,
                        height: "50%",
                        backgroundColor: "black",
                        bottom: 0,
                        left: "50%",
                        transform: "translateX(-50%)",
                        zIndex: 0,
                    }}
                />
            )}

            {leftVisible && (
                <Box
                    sx={{
                        position: "absolute",
                        height: pieceSize / 12,
                        width: "50%",
                        backgroundColor: "black",
                        left: 0,
                        top: "50%",
                        transform: "translateY(-50%)",
                        zIndex: 0,
                    }}
                />
            )}

            {rightVisible && (
                <Box
                    sx={{
                        position: "absolute",
                        height: pieceSize / 12,
                        width: "50%",
                        backgroundColor: "black",
                        right: 0,
                        top: "50%",
                        transform: "translateY(-50%)",
                        zIndex: 0,
                    }}
                />
            )}

        </Box>
    );
}