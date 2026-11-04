import { useSuspenseQuery} from "@tanstack/react-query";
import {findAllGames} from "../../../services/gameService.ts";
import {gamesQueryKey} from "../../../config/api/queryKeys";

export function useGamesList(){
    const { data: games} = useSuspenseQuery({
        queryKey: gamesQueryKey.all,
        queryFn: () => findAllGames(),
    })
    return {games}
}