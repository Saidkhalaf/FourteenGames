import {Avatar, Card, CardContent, Stack, Typography} from "@mui/material";

interface UserMessageProps {
    msg: string;
}

export function UserMessage({msg}: UserMessageProps) {
    return (
        <Stack direction={"row"} alignItems={"center"}>
            <Avatar sx={{ bgcolor: "primary.main",mr:1}} />
            <Card sx={{mt: 1, width:"100%"}}>
                <CardContent>
                    <Typography variant="body2"
                                color={"primary"}>
                        {msg}
                    </Typography>
                </CardContent>
            </Card>
        </Stack>
    )
}