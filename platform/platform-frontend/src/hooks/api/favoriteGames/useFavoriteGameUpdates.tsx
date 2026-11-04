import {useMutation} from "@tanstack/react-query";
import {addFavoriteGame, removeFavoriteGame} from "../../../services/favoriteGameService.ts";
import {invalidateFavoriteGameRelatedKeys} from "../../../config/api/queryKeys/invalidationFunctions.ts";


export function useFavoriteGameUpdates() {
    const {mutate:addFavorite, isPending: addFavoritePending, isError: addFavoriteError} = useMutation(
        {
            mutationFn: async (gameId:string) => {
                return addFavoriteGame(gameId)
            },
            onSuccess: () => {
                invalidateFavoriteGameRelatedKeys()
            }
        }
    )

    const {mutate:removeFavorite, isPending: removeFavoritePending, isError: removeFavoriteError} = useMutation(
        {
            mutationFn: async (gameId:string) => {
                return removeFavoriteGame(gameId)
            },
            onSuccess: () => {
                invalidateFavoriteGameRelatedKeys()
            }
        }
    )

    return {
        addFavoritePending,
        addFavoriteError,
        addFavorite,
        removeFavoritePending,
        removeFavoriteError,
        removeFavorite
    }
}