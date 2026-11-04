import {useMutation, useQueryClient} from "@tanstack/react-query";
import {gameQueryKeys} from "../config/api/querykeys";
import {passTurn} from "../services/gameService.ts";

export function usePassTurn() {
    const queryClient = useQueryClient();
    const {mutate, isPending, isError} = useMutation({
        mutationFn: (gameId: string) => passTurn(gameId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: gameQueryKeys.current});
        },
    })

    return {
        requestPassTurn: mutate,
        isPending,
        isError
    }
}