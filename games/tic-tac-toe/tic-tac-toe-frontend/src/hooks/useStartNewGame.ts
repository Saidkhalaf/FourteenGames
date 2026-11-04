import {useMutation, useQueryClient} from "@tanstack/react-query";
import { startGameWithAi} from "../services/gameService.ts";
import type { MatchRequestAi} from "../models/MatchRequest.ts";
import {gameQueryKeys} from "../config/api/querykeys";
import {useCurrentPlayerSessionStore} from "../store/gameStore.ts";
import type {GameBoard} from "../models/GameBoard.ts";

export function useStartNewGame(){

    const queryClient = useQueryClient();
    const updateCurrentGameId = useCurrentPlayerSessionStore((state) => state.updateCurrentGameId)

    const{mutate,isPending,isError,data: newGame} = useMutation(
        {
            mutationFn: async(player: MatchRequestAi) => {
                return startGameWithAi(player);
            },
            onSuccess:(gameBoard: GameBoard) => {
                updateCurrentGameId(gameBoard.id);
                queryClient.invalidateQueries({queryKey: gameQueryKeys.current});
            }
        }
    )

    return {
        isPending: isPending,
        isError: isError,
        createGame: mutate,
        newGame
    };
}
