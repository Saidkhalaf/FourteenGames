import { Button, Card, CardContent, Stack, Typography, useTheme} from "@mui/material";
import {useNavigate} from "react-router-dom";
import ShieldIcon from '@mui/icons-material/Shield';

interface FriendCardProps {
    userName: string;
    isLobbyHost: boolean;
}

export default function UserCard({userName, isLobbyHost}: FriendCardProps) {
    const theme = useTheme();
    const navigate = useNavigate();
    return (
        <Card
            sx={{
                mt: 2,
                borderRadius: 2,
                boxShadow: 1,
                width: "100%",
                maxWidth: 500,
                color: theme.palette.primary.main,
            }}
        >
            <CardContent>
                <Stack
                    direction={{xs: "column", sm: "row"}}
                    alignItems="center"
                    width="100%"
                    spacing={2}
                >
                    <Stack
                        direction="row"
                        alignItems="center"
                        width={{xs: "100%", sm: "auto"}}
                        flex={1}
                    >
                        {isLobbyHost &&
                            <ShieldIcon/>
                        }
                        <Typography variant="body1"
                                    sx={{ml: 2}}
                                    fontWeight={500}>
                            {userName}
                        </Typography>
                    </Stack>

                    <Stack
                        direction="row"
                        alignItems="center"
                        width={{xs: "100%", sm: "auto"}}
                        spacing={1}
                    >
                        <Button
                            fullWidth
                            variant="contained"
                            color="secondary"
                            onClick={() => navigate("/profile/" + userName)}
                        >
                            Profiel bezoeken
                        </Button>
                    </Stack>
                </Stack>
            </CardContent>
        </Card>
    );
}