import {useMutation} from "@tanstack/react-query";
import {lobbyQueryKey} from "../../../config/api/queryKeys";
import {queryClient} from "../../../config/api";
import {deleteLeaveLobby} from "../../../services/lobbyService.ts";

export function useLeaveLobby() {
    const {mutateAsync:leaveLobby, isPending: leaveLobbyPending, isError: leaveLobbyIsError, error} = useMutation(
        {
            mutationFn: async (lobbyId:string) => {
                return deleteLeaveLobby(lobbyId)
            },
            onSuccess: () => {
                queryClient.invalidateQueries({queryKey: lobbyQueryKey.all});
            }
        }
    )

    return {
        leaveLobbyPending,
        leaveLobbyIsError,
        error,
        leaveLobby
    }
}