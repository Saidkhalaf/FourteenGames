import {useMutation} from "@tanstack/react-query";
import {createLobby} from "../../../services/lobbyService.ts";
import {queryClient} from "../../../config/api";
import { lobbyQueryKey} from "../../../config/api/queryKeys";


export function useCreateLobby() {
    const {mutateAsync, isPending, isError, data} = useMutation(
        {
            mutationFn: async (gameId:string) => {
                return createLobby(gameId)
            },
            onSuccess: () => {
                queryClient.invalidateQueries({queryKey: lobbyQueryKey.all});
            }
        }
    )

    return {
        addLobbyAsync:mutateAsync,
        lobbyPending: isPending,
        lobbyError:isError,
        lobbyData:data
    }
}