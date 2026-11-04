import {favoriteGamesQueryKey, friendRecommendationsQueryKey, friendRequestQueryKey, friendsQueryKey} from "./index.ts";
import {queryClient} from "../index.ts";


export function invalidateFriendRelatedKeys(){
    queryClient.invalidateQueries({queryKey: friendsQueryKey.all});
    queryClient.invalidateQueries({queryKey: friendRequestQueryKey.all});
    queryClient.invalidateQueries({queryKey: friendRecommendationsQueryKey.all});
}
export function invalidateFavoriteGameRelatedKeys(){
    queryClient.invalidateQueries({queryKey: favoriteGamesQueryKey.all});
}