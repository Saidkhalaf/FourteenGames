import {useMutation} from "@tanstack/react-query";
import {lobbyStartGame} from "../../../services/lobbyService.ts";
import {queryClient} from "../../../config/api";
import {lobbyQueryKey} from "../../../config/api/queryKeys";
import type {Lobby} from "../../../models/lobby/Lobby.ts";
interface StartGameVariables {
    lobby: Lobby
    settings: Record<string, unknown>
}

export function useLobbyStartGame() {
    const {mutateAsync:startGame, isPending: startGamePending, isError: startGameError,error} = useMutation(
        {
            mutationFn: async ({lobby, settings }:StartGameVariables,) => {
                return lobbyStartGame(lobby,settings)
            },
            onSuccess: (_data, lobby) => {
                queryClient.invalidateQueries({
                    queryKey: lobbyQueryKey.currentLobby(lobby.lobby.id),
                })
            },
        }
    )

    return {
        startGamePending,
        startGameError,
        error,
        startGame,
    }
}