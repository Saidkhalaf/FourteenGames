import axios from "axios";
import type {Game} from "../models/game/Game.ts";
import type {FavoriteGame} from "../models/game/FavoriteGame.ts";


export async function getFavoriteGames(){
    const {data:games} = await axios.get<FavoriteGame[]>(`/user/favorites`)
    return games
}

export async function isFavoriteGame(gameId: string){
    try {
        await axios.get(`/user/favorites/${gameId}`);
        return true;
    } catch (err: unknown) {
        if (axios.isAxiosError(err) && err.response?.status === 404) {
            return false;
        }
        throw err;
    }
}

export async function addFavoriteGame(gameId:string){
    const {data:game} = await axios.post<Game>(`/user/favorites/${gameId}`)
    return game;
}
export async function removeFavoriteGame(gameId:string){
    await axios.delete(`/user/favorites/${gameId}`)
    return true;
}