import type {CompactGame} from "../../models/game/Game.ts";
import {Stack} from "@mui/material";
import {GameCard} from "../cards/GameCard.tsx";
import type {FavoriteGame} from "../../models/game/FavoriteGame.ts";

interface GameCardListProps {
    games: CompactGame[];
    favorites: FavoriteGame[];
    onSelectGame: () => void;
}

export function GameCardList({games,favorites, onSelectGame}: GameCardListProps) {
    return (
        <Stack direction={"row"} flexWrap="wrap" height={"75%"} sx={{pt:2}}>
            {games.map(game => <GameCard game={game} onSelectGame={onSelectGame} key={game.id} isFavorite={favorites.some(fav => fav.gameId === game.id)}/>)}
        </Stack>
    )
}