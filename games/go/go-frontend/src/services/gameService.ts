import type {MatchRequestAi} from "../models/MatchRequest.ts";
import axios from "axios";
import type {GameState} from "../models/GameState.ts";
import type {MoveRequest} from "../models/MoveRequest.ts";

export async function getOngoingGame() {
    try {
        const {data} = await axios.get<GameState>(`/matches/playing`)
        return data;
    } catch (err) {
        if (axios.isAxiosError(err) && err.response?.status === 404) {
            return null;
        }
        throw err;
    }
}

export async function getGame(gameId: string | null) {
    if (gameId === null) {
        return null;
    }
    const {data: gameState} = await axios.get<GameState>(`/matches/${gameId}`)
    return gameState;
}

export async function startGameWithAi(data: MatchRequestAi) {
    const {data: newGame} = await axios.post<GameState>('/matches/ai', data)
    return newGame
}

export async function makeMove(gameId: string, data: MoveRequest) {
    const {data: newMove} = await axios.patch<GameState>(`/matches/${gameId}`, data);
    return newMove;
}

export async function makeAiMove(gameId: string) {
    const {data: newAiMove} = await axios.patch<GameState>(`/matches/${gameId}/ai`);
    return newAiMove;
}

export async function passTurn(gameId: string) {
    const {data: newMove} = await axios.patch<GameState>(`/matches/${gameId}/pass`);
    return newMove;
}