import {useQuery} from "@tanstack/react-query";
import {lobbyQueryKey} from "../../../config/api/queryKeys";
import {findLobbyByGameSessionId} from "../../../services/lobbyService.ts";
import {pollInterval} from "../../../config/realtime";

export function useLobbyByGameSessionId(gameSessionId:string,enabled:boolean){
    const { data: lobby} = useQuery({
        queryKey: lobbyQueryKey.currentByGameId(gameSessionId),
        queryFn: () => findLobbyByGameSessionId(gameSessionId),
        refetchInterval:pollInterval,
        enabled:enabled
    })
    return {lobby}
}