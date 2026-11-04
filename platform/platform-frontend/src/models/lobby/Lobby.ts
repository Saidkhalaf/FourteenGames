import type {Player} from "./Player.ts";

export interface Lobby{
    id:string;
    gameId:string;
    currentGameSessionId:string;
    lobbyHostId: string;
    players:Player[],
    creationDate: Date,
    maxPlayers:number;
}