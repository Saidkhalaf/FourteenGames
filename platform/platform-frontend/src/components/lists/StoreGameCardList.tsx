import type {CompactGame} from "../../models/game/Game.ts";
import {Stack} from "@mui/material";
import type {FavoriteGame} from "../../models/game/FavoriteGame.ts";
import {StoreGameCard} from "../cards/StoreGameCard.tsx";

interface GameCardListProps {
    games: CompactGame[];
    favorites: FavoriteGame[];
}

export function StoreGameCardList({games,favorites}: GameCardListProps) {
    return (
        <Stack direction={"row"} flexWrap="wrap" height={"75%"} sx={{pt:2}}>
            {games.map(game => <StoreGameCard game={game} key={game.id} isFavorite={favorites.some(fav => fav.gameId === game.id)}/>)}
        </Stack>
    )
}