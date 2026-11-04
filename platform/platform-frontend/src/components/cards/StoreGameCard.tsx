import type {CompactGame} from "../../models/game/Game.ts";
import {Card, CardContent, CardActionArea, CardMedia, Typography, Stack, useTheme, Button} from "@mui/material"
import VideogameAssetIcon from '@mui/icons-material/VideogameAsset';
import {ConfirmationDialog} from "../dialogs/ConfirmationDialog.tsx";
import {useState} from "react";
import {creditName} from "../../config/theme/names.ts";
import {useNotificationStore} from "../../stores/notificationStore.ts";
import {usePlatformUser} from "../../hooks/usePlatformUser.tsx";
import {useBuyGame} from "../../hooks/api/user/useBuyGame.tsx";

interface GameCardProps {
    game: CompactGame
    isFavorite: boolean;
}

export function StoreGameCard({game}: GameCardProps) {
    const theme = useTheme();
    const addNotification = useNotificationStore((state) => state.addNotification);
    const [isConfirming, setIsConfirming] = useState(false);
    const {platformUser} = usePlatformUser();
    const {buyGame} = useBuyGame();

    return (
        <>
            <Card
                sx={{
                    width: {lg: "20%", xs: "40%"},
                    height: "60%",
                    marginRight: "5%",
                    marginBottom: "5%",
                    cursor: "pointer",
                    borderRadius: 4
                }}>
                <CardActionArea sx={{height: "75%", overflow: "hidden"}}>
                    {game.icon == "" ?
                        <Stack alignItems={"center"}
                               sx={{marginY: "1rem"}}>
                            <VideogameAssetIcon sx={{color: theme.palette.text.secondary}}/>
                        </Stack>
                        :
                        <CardMedia component="img"
                                   sx={{
                                       padding: "5%",
                                       height: 250,
                                       width: "100%",
                                       marginX: "auto",
                                       objectFit: "cover",
                                   }}
                                   image={game.icon}/>
                    }
                </CardActionArea>
                <CardContent sx={{height: "25%", boxShadow: "5"}}>
                    <Stack flexDirection={"row"}
                           alignItems={"center"}
                           justifyContent={"space-between"}>
                        <Typography color={theme.palette.text.secondary}>{game.name}</Typography>
                    </Stack>
                    <Stack direction={"row"}
                           alignItems={"center"}
                           justifyContent={"space-between"}>
                        <Typography
                            sx={{fontWeight: "bold"}}
                            color={theme.palette.primary.main}
                        >{game.price.toFixed(2)} {creditName}</Typography>
                        <Button
                            variant={"contained"}
                            onClick={() => setIsConfirming(true)}
                        >
                            Kopen
                        </Button>
                    </Stack>
                </CardContent>
            </Card>
            <ConfirmationDialog isOpen={isConfirming}
                                onAccept={
                                    () => {
                                        if(platformUser.credits < game.price){
                                            addNotification({
                                                message:"Je saldo is te laag...",
                                                severity:"error"
                                            })
                                        }
                                        buyGame(game.id);
                                        setIsConfirming(false)
                                    }
                                }
                                onClose={() => setIsConfirming(false)}
                                confirmationMessage={`Aankoop bevestigen`}
                                confirmationDescription={`Weet je zeker dat je ${game.name} wilt kopen?`}
                                warningMessage={`Dit kost ${game.price.toFixed(2)} ${creditName}`}
            />
        </>
    )
}