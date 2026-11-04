import {useMutation, useQueryClient} from "@tanstack/react-query";
import type {MoveRequest} from "../models/MoveRequest.ts";
import {makeMove} from "../services/gameService.ts";
import {gameQueryKeys} from "../config/api/querykeys";

interface MoveVariables{
    gameId: string;
    moveRequest: MoveRequest;
}

export function usePlaceStone() {
    const queryClient = useQueryClient();

    const { mutate, isPending, isError } = useMutation({
        mutationFn: async ({ gameId, moveRequest }: MoveVariables) => {
            return makeMove(gameId, moveRequest);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey:gameQueryKeys.current });
        },
    });

    return {
        isPending,
        isError,
        requestMove: mutate,
    };
}