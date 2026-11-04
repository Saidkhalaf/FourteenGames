import {Button, Card, CardContent, Stack, Typography, useTheme} from "@mui/material";
import {useState} from "react";
import {useFriendList} from "../hooks/api/friends/useFriendList.tsx";
import FriendCard from "../components/cards/FriendCard.tsx";
import {useFriendRequestList} from "../hooks/api/friends/useFriendRequestList.tsx";
import FriendRequestCard from "../components/cards/FriendRequestCard.tsx";
import {NewFriendDialog} from "../components/dialogs/NewFriendDialog.tsx";
import {useSecurityStore} from "../stores/securityStore.ts";

export function FriendListPage() {
    const theme = useTheme();
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const {friendList} = useFriendList();
    const {friendRequests} = useFriendRequestList();
    const [isAddingNewFriends, setIsAddingNewFriends] = useState(false);

    return (
        <>
            <Card sx={{
                maxWidth: "100%",
                mt: 5,
                borderRadius: 3,
                overflow: 'hidden',
                color: theme.palette.primary.main
            }}>
                <CardContent>
                    <Stack
                        direction={{xs: "column", md: "row"}}
                        alignItems={"center"}
                        justifyContent={"space-between"}>
                        <Typography variant={"h4"}><span style={{fontWeight: "bold"}}>{loggedInUser?.username}'s</span> vrienden</Typography>
                        <Stack direction={"row"}>
                            <Button variant={"contained"}
                                    color={"primary"}
                                    sx={{m: 2}}
                                    onClick={() => setIsAddingNewFriends(true)}>
                                Nieuwe vrienden toevoegen
                            </Button>
                        </Stack>
                    </Stack>


                    {friendRequests.friends.length != 0 &&
                        <Stack
                            direction={"column"}
                            sx={{mb: 2, mt: 2}}>
                            <Typography variant={"h5"}>Openstaande vriendschaps verzoeken</Typography>
                            <Stack
                                direction={"row"}
                                flexWrap={"wrap"}>
                                {friendRequests.friends.map((friendRequest) =>
                                    <FriendRequestCard
                                        data-testid={"friend-request-card"}
                                        userName={friendRequest.userName}
                                        key={"friendRequest" + friendRequest.userName}
                                    />
                                )
                                }
                            </Stack>
                        </Stack>


                    }

                    {friendList.friends.length === 0 &&
                        <Typography
                            variant={"h5"}
                            data-testid={"no-friend-warning"}>Het lijkt er op dat je nog geen vrienden hebt
                            toegevoegd...</Typography>
                    }

                    <Stack
                        direction={"column"}
                    >
                        {friendList.friends.length != 0 &&
                            <Typography variant={"h5"}>Jouw vrienden:</Typography>
                        }
                        <Stack
                            flexWrap={"wrap"}
                            direction={"row"}
                            gap={2}>
                            {friendList.friends.map((friend) => {
                                return <FriendCard data-testid={"friend-card"}
                                                   key={friend.userName}
                                                   userName={friend.userName}/>
                            })}
                        </Stack>
                    </Stack>

                </CardContent>
            </Card>
            <NewFriendDialog isOpen={isAddingNewFriends}
                             onClose={() => setIsAddingNewFriends(false)}/>
        </>
    )
}