import {useMutation, useQueryClient} from "@tanstack/react-query";
import { makeMove} from "../services/gameService.ts";
import { gameQueryKeys } from "../config/api/querykeys";
import type { MoveRequest } from "../models/MoveRequest.ts";
import type {GameBoard} from "../models/GameBoard.ts";

interface MoveVariables{
    gameId: string;
    moveRequest: MoveRequest;
}

export function useNewGameMove() {
    const queryClient = useQueryClient();

    const { mutate, isPending, isError } = useMutation({
        mutationFn: async ({ gameId, moveRequest }: MoveVariables) => {
            return makeMove(gameId, moveRequest);
        },
        onSuccess: (gameState: GameBoard) => {
            queryClient.invalidateQueries({ queryKey:gameQueryKeys.currentWithGameId(gameState.id) });
        },
    });

    return {
        isPending,
        isError,
        requestMove: mutate,
    };
}