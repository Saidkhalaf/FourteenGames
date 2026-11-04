import {useQuery} from "@tanstack/react-query";
import {gameQueryKeys} from "../config/api/querykeys";
import {getGame} from "../services/gameService.ts";
import {refetchInterval} from "../config/realtime";

export function useGameBoard(gameId:string) {
    const {data: gameState, isError, isPending} = useQuery({
        queryKey: gameQueryKeys.current,
        queryFn: () => getGame(gameId),
        refetchInterval:refetchInterval
    });

    return {
        isError,
        isGamePending: isPending,
        gameState,
    };
}