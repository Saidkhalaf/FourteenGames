import { useSuspenseQuery} from "@tanstack/react-query";
import {friendRecommendationsQueryKey} from "../../../config/api/queryKeys";
import {getFriendRecommendations} from "../../../services/friendService.ts";

export function useFriendRecommendations(nameQuery: string){
    const { data: recommendations} = useSuspenseQuery({
        queryKey: friendRecommendationsQueryKey.all,
        queryFn: () => getFriendRecommendations(nameQuery),

    })
    return {recommendations}
}