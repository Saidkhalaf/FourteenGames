import {Avatar, Button, Card, CardContent, CircularProgress, IconButton, Stack, Typography, useTheme} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useDeleteFriend} from "../../hooks/api/friends/useDeleteFriend.tsx";
import DeleteIcon from '@mui/icons-material/Delete';
import {useState} from "react";
import {ConfirmationDialog} from "../dialogs/ConfirmationDialog.tsx";

interface FriendCardProps {
    userName: string;
}

export default function FriendCard({userName}: FriendCardProps) {
    const theme = useTheme();
    const navigate = useNavigate();
    const [isConfirming, setIsConfirming] = useState(false);
    const {removeFriend, removeFriendIsError, removeFriendIsPending} = useDeleteFriend();
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
                    width="100%"
                    spacing={2}
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
                        direction="row"
                        alignItems="center"
                        width={{ xs: "100%", sm: "auto" }}
                        spacing={1}
                    >
                        <Button
                            fullWidth
                            variant="contained"
                            color="primary"
                            onClick={() => navigate("/profile/" + userName)}
                        >
                            Profiel bezoeken
                        </Button>

                        {!removeFriendIsError && !removeFriendIsPending ? (
                            <IconButton
                                sx={{ background: theme.palette.secondary.main }}
                                onClick={() => setIsConfirming(true)}
                            >
                                <DeleteIcon />
                            </IconButton>
                        ) : (
                            <CircularProgress size={24} />
                        )}
                    </Stack>
                </Stack>
            </CardContent>

            <ConfirmationDialog
                confirmationMessage={"Weet je het zeker?"}
                warningMessage={`Deze actie zal ${userName} uit jouw vriendenlijst verwijderen.`}
                isOpen={isConfirming}
                onAccept={() => removeFriend(userName)}
                onClose={() => setIsConfirming(false)}/>
        </Card>
    );
}