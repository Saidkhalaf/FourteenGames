import axios from "axios";
import type {CompactGame, Game} from "../models/game/Game.ts";

export async function checkGameReachable(gameUrl: string): Promise<boolean>{
    try{
        await axios.get(gameUrl);
        return true;
    }catch(e){
        console.error('Error fetching game:', e);
        return false;
    }
}

export async function getGame(gameId:string){
    const {data:game} = await axios.get<Game>(`/games/${gameId}`)
    return game
}

export async function findAllGames(){
    const {data: games} = await axios.get<CompactGame[]>('/games')
    return games
}