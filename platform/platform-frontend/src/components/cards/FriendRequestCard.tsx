import {Avatar, Button, Card, CardContent, Stack, Typography, useTheme} from "@mui/material";
import {useFriendRequestResponse} from "../../hooks/api/friends/useFriendRequestResponse.tsx";

interface FriendCardProps {
    userName: string;
}
export default function FriendRequestCard({ userName }: FriendCardProps) {
    const theme = useTheme();
    const { acceptRequest, denyRequest } = useFriendRequestResponse();

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
                    direction={{ xs: "column", sm: "row" }}
                    alignItems="center"
                    spacing={2}
                    width="100%"
                >
                    <Stack
                        direction="row"
                        alignItems="center"
                        width={{ xs: "100%", sm: "auto" }}
                        flex={1}
                    >
                        <Avatar sx={{ bgcolor: "primary.main", width: 32, height: 32 }} />
                        <Typography variant="body1" sx={{ ml: 2 }} fontWeight={500}>
                            {userName}
                        </Typography>
                    </Stack>

                    <Stack
                        direction={{ xs: "column", sm: "row" }}
                        spacing={1}
                        width={{ xs: "100%", sm: "auto" }}
                    >
                        <Button
                            fullWidth
                            variant="contained"
                            color="primary"
                            onClick={() => acceptRequest(userName)}
                        >
                            Accepteren
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            color="secondary"
                            onClick={() => denyRequest(userName)}
                        >
                            Weigeren
                        </Button>
                    </Stack>
                </Stack>
            </CardContent>
        </Card>
    );
}