import {Button, CircularProgress,Stack, TextField, Typography, useTheme} from "@mui/material";
import {useSendFriendRequest} from "../../hooks/api/friends/useSendFriendRequest.tsx";
import {type FormEvent, useState} from "react";

export function AddOnUserName() {
    const theme = useTheme();
    const {sendFriendRequest, sendFriendRequestIsPending, sendFriendRequestIsError, reset} = useSendFriendRequest();
    const [username, setUsername] = useState("");

    const handleFormSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!username) return;
        sendFriendRequest(username);
    };

    return (
        <>
            <Typography variant="h6"
                        sx={{color: theme.palette.primary.main}}>
                Op gebruikersnaam
            </Typography>
            {!sendFriendRequestIsPending && !sendFriendRequestIsError &&
                <form onSubmit={handleFormSubmit}>
                    <Stack direction="column"
                           spacing={2}
                           alignItems={"center"}
                           sx={{
                               m: 2,
                           }}>
                        <TextField
                            label="Gebruikersnaam"
                            variant="outlined"
                            fullWidth
                            value={username}
                            sx={{
                                '& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline': {
                                    borderColor: theme.palette.primary.main,
                                },
                                '& .MuiOutlinedInput-input': {
                                    color: theme.palette.primary.main,
                                }
                            }}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <Button
                            fullWidth
                            variant={"contained"}
                            type="submit"
                            color={"primary"}
                        >
                            Verzoek versturen
                        </Button>
                    </Stack>
                </form>
            }

            {sendFriendRequestIsError &&
                <>
                    <Typography sx={{
                        m: 2,
                        color: theme.palette.primary.main
                    }}>
                        Deze gebruiker werd niet gevonden...
                    </Typography>
                    <Button variant={"contained"}
                            onClick={() => {
                                setUsername("")
                                reset();
                            }}>
                        Opnieuw proberen
                    </Button>
                </>
            }

            {
                sendFriendRequestIsPending &&
                <>
                    <Stack direction="column"
                           justifyContent={"center"}
                           alignItems={"center"}
                           spacing={2}
                           sx={{m: 2}}>
                        <CircularProgress/>
                    </Stack>
                </>
            }

        </>
    )
}