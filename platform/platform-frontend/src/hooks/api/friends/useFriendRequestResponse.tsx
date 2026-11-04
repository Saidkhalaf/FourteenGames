import {useMutation} from "@tanstack/react-query";
import {invalidateFriendRelatedKeys} from "../../../config/api/queryKeys";
import {acceptFriendRequest, denyFriendRequest} from "../../../services/friendService.ts";


export function useFriendRequestResponse() {
    const {mutate:acceptRequest, isPending: addRequestIsPending, isError: addRequestIsError} = useMutation(
        {
            mutationFn: async (friendUsername:string) => {
                return acceptFriendRequest(friendUsername)
            },
            onSuccess: () => {
                invalidateFriendRelatedKeys();
            }
        }
    )

    const {mutate:denyRequest, isPending: denyRequestIsPending, isError: denyRequestIsError} = useMutation(
        {
            mutationFn: async (friendUsername:string) => {
                return denyFriendRequest(friendUsername)
            },
            onSuccess: () => {
                invalidateFriendRelatedKeys();
            }
        }
    )

    return {
        addRequestIsPending,
        addRequestIsError,
        acceptRequest,
        denyRequestIsPending,
        denyRequestIsError,
        denyRequest
    }
}