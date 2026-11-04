import {useMutation, useQueryClient} from "@tanstack/react-query";
import {favoriteGamesQueryKey, gamesQueryKey, ownedGamesQueryKeys, currentUserDataQueryKey} from "../../../config/api/queryKeys";
import {buyGame} from "../../../services/userService.ts";

export function useBuyGame() {
    const queryClient = useQueryClient();

    const {mutate, isPending, isError, isSuccess} = useMutation({
        mutationFn: (gameId:string) => buyGame(gameId),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: gamesQueryKey.all
            });
            queryClient.invalidateQueries({
                queryKey: ownedGamesQueryKeys.all
            });
            queryClient.invalidateQueries({
                queryKey: favoriteGamesQueryKey.all
            });
            queryClient.invalidateQueries({
                queryKey: currentUserDataQueryKey.current
            });

        }
    })

    return {
        buyGame: mutate,
        isPending,
        isError,
        isSuccess,
    };
}
