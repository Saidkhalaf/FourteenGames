import {useMutation} from "@tanstack/react-query";
import {startTrainingGame} from "../../../services/lobbyService.ts";

export function useStartTrainingGame() {
    const {mutateAsync: startTraining, isPending: isStartingTraining, isError: startTrainingError, error} = useMutation({
        mutationFn: (gameId: string) => startTrainingGame(gameId),
    });

    return {
        startTraining,
        isStartingTraining,
        startTrainingError,
        error,
    };
}
