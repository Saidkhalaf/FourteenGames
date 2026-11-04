import {useQuery} from "@tanstack/react-query";
import {gameQueryKeys} from "../config/api/querykeys";
import {getOngoingGame} from "../services/gameService.ts";

export function useGetOngoingGameBoard() {
    const {data: gameState, isError, isPending} = useQuery({
        queryKey: gameQueryKeys.current,
        queryFn: () => getOngoingGame(),
    });

    return {
        isError,
        isGamePending: isPending,
        gameState,
    };
}