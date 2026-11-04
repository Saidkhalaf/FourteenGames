import {Avatar, Card, CardContent, Stack, Typography, useTheme} from "@mui/material";

interface BotMessageProps {
    msg: string;
}

export function BotMessage({msg}: BotMessageProps) {
    const theme = useTheme();
    return (
        <Stack direction={"row"} alignItems={"center"}>
            <Card sx={{mt:1,mr:1,background:theme.palette.primary.main}}>
                <CardContent>
                    <Typography variant="body2">
                        {msg}
                    </Typography>
                </CardContent>
            </Card>
            <Avatar sx={{ bgcolor: "primary.main"}} />
        </Stack>
    )
}