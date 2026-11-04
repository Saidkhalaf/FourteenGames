import { useSuspenseQuery} from "@tanstack/react-query";
import {favoriteGamesQueryKey} from "../../../config/api/queryKeys";
import {getFavoriteGames} from "../../../services/favoriteGameService.ts";

export function useFavoriteGames(){
    const { data: favorites} = useSuspenseQuery({
        queryKey: favoriteGamesQueryKey.all,
        queryFn: () => getFavoriteGames(),
    })
    return {favorites}
}