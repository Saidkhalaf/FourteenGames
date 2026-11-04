import {useSuspenseQuery} from "@tanstack/react-query";
import { getGame} from "../../../services/gameService.ts";
import {gamesQueryKey} from "../../../config/api/queryKeys";

export function useGame(gameId: string){
    const {isLoading, isError, data: game} = useSuspenseQuery({
        queryKey: gamesQueryKey.currentGame(gameId),
        queryFn: () => getGame(gameId),
    })
    return {isLoading, isError, game}
}