import type {CompactGame} from "../../src/models/game/Game.ts";
import type {FavoriteGame} from "../../src/models/game/FavoriteGame.ts";
import type {Achievement} from "../../src/models/achievement/Achievement.ts";

export const achievement: Achievement={
    id:"5",
    name:"Let's Tetris",
    description:"Start tetris for the frist time!"
};

export const compactGame1: CompactGame = {
    id: "1",
    name: "Tic Tac Toe",
    icon: "icon.png",
    genre: "Puzzle",
    url: "http://localhost:1234",
    achievements:[],
    price:5,
    configurableSettings: {}
}

export const compactGame2: CompactGame = {
    id: "2",
    name: "Go",
    icon: "GoIcon.png",
    genre: "Strategy",
    url: "http://localhost:1235",
    achievements:[],
    price:19.99,
    configurableSettings: {}
}
export const favoriteGame1: FavoriteGame={
    id:"5",
    gameId:"1",
}