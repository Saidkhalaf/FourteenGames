import { useSuspenseQuery} from "@tanstack/react-query";
import {lobbyQueryKey} from "../../../config/api/queryKeys";
import {findLobby} from "../../../services/lobbyService.ts";
import {pollInterval} from "../../../config/realtime";

export function useLobby(lobbyId:string){
    const { data: lobby} = useSuspenseQuery({
        queryKey: lobbyQueryKey.currentLobby(lobbyId),
        queryFn: () => findLobby(lobbyId),
        refetchInterval:pollInterval
    })
    return {lobby}
}