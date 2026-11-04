import {useMutation, useQueryClient} from "@tanstack/react-query";
import {makeAiMove} from "../services/gameService.ts";
import {gameQueryKeys} from "../config/api/querykeys";
import type {GameState} from "../models/GameState.ts";

export function useAiMove() {
    const queryClient = useQueryClient();
    const {mutate, isPending, isError} = useMutation({
        mutationFn: (gameId: string) => makeAiMove(gameId),
        onSuccess: (gameState: GameState) => {
            queryClient.invalidateQueries({ queryKey: gameQueryKeys.currentWithGameId(gameState.id)});
        },
    })

    return {
        requestAiMove:mutate,
        isPending,
        isError
    }
}