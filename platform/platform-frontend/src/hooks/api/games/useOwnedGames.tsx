import {getOwnedGames} from "../../../services/userService.ts";
import {useSuspenseQuery} from "@tanstack/react-query";
import { ownedGamesQueryKeys} from "../../../config/api/queryKeys";

export function useOwnedGames(){
    const { data: ownedGames} = useSuspenseQuery({
        queryKey: ownedGamesQueryKeys.all,
        queryFn: () => getOwnedGames(),
    })
    return {ownedGames}
}