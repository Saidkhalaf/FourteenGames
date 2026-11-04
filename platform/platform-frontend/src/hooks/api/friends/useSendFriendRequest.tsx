import {useMutation} from "@tanstack/react-query";
import {invalidateFriendRelatedKeys} from "../../../config/api/queryKeys";
import {postFriendRequest} from "../../../services/friendService.ts";


export function useSendFriendRequest() {
    const {mutate:sendFriendRequest, isPending: sendFriendRequestIsPending, isError: sendFriendRequestIsError, reset} = useMutation(
        {
            mutationFn: async (friendUsername:string) => {
                return postFriendRequest(friendUsername)
            },
            onSuccess: () => {
                invalidateFriendRelatedKeys();
            }
        }
    )


    return {
        sendFriendRequestIsPending,
        sendFriendRequestIsError,
        reset,
        sendFriendRequest
    }
}