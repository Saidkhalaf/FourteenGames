import {useMutation, useQueryClient} from "@tanstack/react-query";
import {gameQueryKeys} from "../config/api/querykeys";
import type {MatchRequestAi} from "../models/MatchRequest.ts";
import {startGameWithAi} from "../services/gameService.ts";
import type {GameState} from "../models/GameState.ts";
import {useCurrentPlayerSessionStore} from "../store/gameStore.ts";


export function useStartNewAiGame(){
    const queryClient = useQueryClient();
    const updateCurrentGameId = useCurrentPlayerSessionStore((state) => state.updateCurrentGameId)

    const{mutateAsync,isPending,isError,data: newGame} = useMutation(
        {
            mutationFn: async(request: MatchRequestAi) => {
                return startGameWithAi(request);
            },
            onSuccess:(gameBoard: GameState) => {
                updateCurrentGameId(gameBoard.id);
                queryClient.invalidateQueries({queryKey: gameQueryKeys.current});
            }
        }
    )

    return {
        isPending: isPending,
        isError: isError,
        createGame: mutateAsync,
        newGame
    };
}