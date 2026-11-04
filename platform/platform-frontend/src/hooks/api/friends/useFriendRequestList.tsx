import { useSuspenseQuery} from "@tanstack/react-query";
import {friendRequestQueryKey} from "../../../config/api/queryKeys";
import {findAllOpenFriendRequests} from "../../../services/friendService.ts";
import {pollInterval} from "../../../config/realtime";

export function useFriendRequestList(){
    const { data: friendRequests} = useSuspenseQuery({
        queryKey: friendRequestQueryKey.all,
        queryFn: () => findAllOpenFriendRequests(),
        refetchInterval:pollInterval
    })
    return {friendRequests}
}