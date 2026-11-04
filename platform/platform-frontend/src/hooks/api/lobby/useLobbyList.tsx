import { useSuspenseQuery} from "@tanstack/react-query";
import {lobbyQueryKey} from "../../../config/api/queryKeys";
import {findAllLobbies} from "../../../services/lobbyService.ts";
import {pollInterval} from "../../../config/realtime";

export function useLobbyList(){
    const { data: lobbies} = useSuspenseQuery({
        queryKey: lobbyQueryKey.all,
        queryFn: () => findAllLobbies(),
        refetchInterval: pollInterval
    })
    return {lobbies}
}