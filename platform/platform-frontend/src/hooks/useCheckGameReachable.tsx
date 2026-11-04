import {checkGameReachable} from "../services/gameService.ts";
import {useSuspenseQuery} from "@tanstack/react-query";

export function useCheckGameReachable(gameUrl: string){
    const {data: isReachable} = useSuspenseQuery({
        queryKey: ["checkWebsiteUp", gameUrl],
        queryFn: () => checkGameReachable(gameUrl),
    })
    return {isReachable}
}