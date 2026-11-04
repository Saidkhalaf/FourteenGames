import {useQuery} from "@tanstack/react-query";
import {getGame} from "../services/gameService.ts";
import {useCurrentPlayerSessionStore} from "../store/gameStore.ts";
import {gameQueryKeys} from "../config/api/querykeys";

export function useGameBoard() {
    const currentGameId = useCurrentPlayerSessionStore((state) => state.currentGameId)
    const {data: gameState, isError, isPending} = useQuery({
        queryKey: gameQueryKeys.currentWithGameId(currentGameId!),
        queryFn: () => {
            return getGame(currentGameId);
        },
        enabled: !!currentGameId
    });

    return {
        isError,
        isGamePending: isPending,
        gameState
    }
}