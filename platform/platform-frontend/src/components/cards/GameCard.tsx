import type {CompactGame} from "../../models/game/Game.ts";
import {Card, CardContent, CardActionArea, CardMedia, Typography, Stack, useTheme} from "@mui/material"
import VideogameAssetIcon from '@mui/icons-material/VideogameAsset';
import {useFavoriteGameUpdates} from "../../hooks/api/favoriteGames/useFavoriteGameUpdates.tsx";
import {FavoriteButton} from "../FavoriteButton.tsx";
import {useSelectionStore} from "../../stores/selectionStore.ts";

interface GameCardProps {
    game: CompactGame
    isFavorite: boolean;
    onSelectGame: () => void;
}

export function GameCard({game, isFavorite, onSelectGame}: GameCardProps) {
    const theme = useTheme();
    const setSelectedGameId = useSelectionStore((state) => state.setSelectedGameId);
    const {addFavorite, removeFavoriteError, addFavoriteError, removeFavorite} = useFavoriteGameUpdates();

    if (removeFavoriteError || addFavoriteError) {
        throw new Error("Error with updating favorites.");
    }

    function handleFavoriteChange() {
        if (isFavorite) {
            removeFavorite(game.id);
        } else {
            addFavorite(game.id);
        }
    }

    function handleSelectGame() {
        onSelectGame();
    }


    return (
        <Card
            onClick={() => {
                setSelectedGameId(game.id);
                handleSelectGame();
            }
            }
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
                               sx={{padding: "5%", height: 250, width: "100%", marginX: "auto", objectFit: "cover",}}
                               image={game.icon}/>
                }
            </CardActionArea>
            <CardContent sx={{height: "25%", boxShadow: "5"}}>
                <Stack flexDirection={"row"}
                       alignItems={"center"}
                       justifyContent={"space-between"}>
                    <Typography color={theme.palette.text.secondary}>{game.name}</Typography>

                    <FavoriteButton
                        onClick={handleFavoriteChange}
                        selected={isFavorite}
                    />
                </Stack>
            </CardContent>
        </Card>
    )
}