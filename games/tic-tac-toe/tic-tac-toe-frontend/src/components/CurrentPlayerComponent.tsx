import {Typography, Card, Avatar, CardContent, useTheme} from "@mui/material";

interface CurrentPlayerComponentProps {
    currentUser: string;
    isCurrentlyPlaying: boolean;
}

export function CurrentPlayerComponent({isCurrentlyPlaying, currentUser}: CurrentPlayerComponentProps) {
    const theme = useTheme();
    return (
        <Card
            sx={{
                display: "flex",
                alignItems: "center",
                padding: 2,
                width: "25%",
                mt: 2,
                position: "absolute",
                top: 50,
                left: 20,
                color: isCurrentlyPlaying ? theme.palette.primary.contrastText :  theme.palette.secondary.main,
                border: `5px solid ${isCurrentlyPlaying ?   theme.palette.primary.contrastText :  theme.palette.secondary.main}`
            }}
        >
            <Avatar
                src="https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"
                alt="Player"
                sx={{width: 64, height: 64, marginRight: 2}}
            />

            <CardContent sx={{padding: 0}}>
                {isCurrentlyPlaying ?
                    <Typography sx={{fontWeight: "bold"}}>
                        Het is jouw beurt!
                    </Typography>
                    :
                    <Typography>
                        Speler aan beurt: {currentUser}
                    </Typography>
                }
                <Typography variant="h4">X</Typography>
            </CardContent>
        </Card>
    );
}