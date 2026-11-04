import {Avatar, Button, Card, CardContent, CircularProgress, Stack, Typography, useTheme} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useSendFriendRequest} from "../../hooks/api/friends/useSendFriendRequest.tsx";

interface FriendCardProps {
    userName: string;
}

export default function NewFriendCard({userName}: FriendCardProps) {
    const theme = useTheme();
    const navigate = useNavigate();
    const {sendFriendRequest, sendFriendRequestIsPending} = useSendFriendRequest();

    return (
        <Card
            sx={{
                mt: 4,
                borderRadius: 2,
                boxShadow: 1,
                width: "100%",
                color: theme.palette.primary.main,
            }}
        >
            <CardContent>
                <Stack direction="row"
                       alignItems="center"
                       width="100%">
                    <Stack flex={0.25}
                           alignItems="center">
                        <Avatar sx={{bgcolor: "primary.main", width: 32, height: 32}}/>
                    </Stack>
                    <Stack flex={0.75}>
                        <Typography variant="body1"
                                    sx={{ml: 1}}
                                    fontWeight={500}>
                            {userName}
                        </Typography>
                    </Stack>
                    <Button
                        variant={"contained"}
                        color={"secondary"}
                        sx={{mr: 2}}
                        onClick={() => navigate("/profile/" + userName)}
                    >
                        Profiel bezoeken
                    </Button>
                    {!sendFriendRequestIsPending?
                        < Button
                        variant={"contained"}
                        color={"primary"}
                        onClick={() => sendFriendRequest(userName)}
                        >
                        Vriendschapsverzoek versturen
                        </Button>
                        :
                        <CircularProgress/>
                    }
                </Stack>
            </CardContent>
        </Card>
    );
}