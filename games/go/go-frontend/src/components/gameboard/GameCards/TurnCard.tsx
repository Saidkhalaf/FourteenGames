import {Button, CircularProgress, Paper, Stack, Typography} from "@mui/material";
import type {GameState} from "../../../models/GameState.ts";

interface TurnCardProps {
    atTurn: boolean,
    aiPending: boolean,
    size: number,
    id: string,
    onClick: () => void,
    canPass: boolean,
    isLastTurnPassed: boolean,
    board: GameState
}

export function TurnCard(
    {
        atTurn,
        aiPending,
        size,
        id,
        onClick,
        canPass,
        isLastTurnPassed,
        board
    }: TurnCardProps) {
    return <Paper elevation={3} sx={{p: 2}}>
        <Stack spacing={1}>
            {atTurn ? (
                <Stack spacing={0.5}>
                    <Typography variant="h6">
                        {board.player1Id} turn
                    </Typography>
                    {isLastTurnPassed && (
                        <Typography variant="caption" color="info.main">
                            Opponent passed their turn
                        </Typography>
                    )}
                </Stack>
            ) : (
                board.isAiGame ?
                <Stack direction="row" spacing={1} alignItems="center">
                    {aiPending && <CircularProgress size={20}/>}
                    <Typography variant="h6">AI is thinking…</Typography>
                </Stack>
                    :
                    <Stack direction="row" spacing={1} alignItems="center">
                        <Typography variant="h6">{board.player2Id} is at turn</Typography>
                    </Stack>
            )}
            <Typography variant="body2">{size}x{size} - game: {id}</Typography>
            <Stack direction="row" spacing={1}>
                <Button
                    variant="contained"
                    onClick={onClick}
                    disabled={!canPass}
                >
                    Pass
                </Button>
            </Stack>
        </Stack>
    </Paper>;
}