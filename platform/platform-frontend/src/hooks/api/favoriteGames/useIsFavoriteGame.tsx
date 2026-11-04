import {useSuspenseQuery} from "@tanstack/react-query";
import {favoriteGamesQueryKey} from "../../../config/api/queryKeys";
import {isFavoriteGame} from "../../../services/favoriteGameService.ts";

export function useGameIsFavorite(gameId: string) {
    const {
        isLoading, isError, data: isFavorite} = useSuspenseQuery<boolean>({
        queryKey: favoriteGamesQueryKey.currentGame(gameId),
        queryFn: () => isFavoriteGame(gameId),
    });

    return { isLoading, isError, isFavorite };
}