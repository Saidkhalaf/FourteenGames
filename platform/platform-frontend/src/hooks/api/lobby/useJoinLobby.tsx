import {useMutation} from "@tanstack/react-query";
import {lobbyQueryKey} from "../../../config/api/queryKeys";
import {queryClient} from "../../../config/api";
import {patchJoinLobby} from "../../../services/lobbyService.ts";

export function useJoinLobby() {
    const {mutateAsync:joinLobby, isPending: joinLobbyPending, isError: joinLobbyIsError, error} = useMutation(
        {
            mutationFn: async (lobbyId:string) => {
                return patchJoinLobby(lobbyId)
            },
            onSuccess: () => {
                queryClient.invalidateQueries({queryKey: lobbyQueryKey.all});
            }
        }
    )

    return {
        joinLobbyPending,
        joinLobbyIsError,
        error,
        joinLobby
    }
}