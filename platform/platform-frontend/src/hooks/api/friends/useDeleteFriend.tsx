import {useMutation} from "@tanstack/react-query";
import {invalidateFriendRelatedKeys} from "../../../config/api/queryKeys";
import {deleteFriend} from "../../../services/friendService.ts";


export function useDeleteFriend() {
    const {mutate:removeFriend, isPending: removeFriendIsPending, isError: removeFriendIsError} = useMutation(
        {
            mutationFn: async (friendUsername:string) => {
                return deleteFriend(friendUsername)
            },
            onSuccess: () => {
                invalidateFriendRelatedKeys();
            }
        }
    )

    return {
        removeFriendIsPending,
        removeFriendIsError,
        removeFriend
    }
}