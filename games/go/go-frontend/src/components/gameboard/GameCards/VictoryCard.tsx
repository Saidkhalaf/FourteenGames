import {Button, CircularProgress, Paper, Stack, Typography} from "@mui/material";

interface VictoryCardProps {
    winner: string,
    score: number,
    onClick: () => void,
    disabled: boolean,
    isAiGame: boolean,
    isLoading: boolean
}

export function VictoryCard(
    {
        winner,
        score,
        onClick,
        disabled,
        isAiGame,
        isLoading
    }: VictoryCardProps) {

    return <Paper elevation={4}
                  sx={{p: 2}}>
        <Stack spacing={1}>
            <Typography variant="h5">Winner</Typography>
            <Typography variant="h6">{winner}</Typography>
            <Typography variant="body1">Score: {score}</Typography>
            {isAiGame &&
                <>
                    {isLoading ?
                        <CircularProgress/>
                        :
                        <Button variant="contained"
                                onClick={onClick}
                                disabled={disabled}>
                            Play again
                        </Button>
                }
                </>
            }
        </Stack>
    </Paper>;
}