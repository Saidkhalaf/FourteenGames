//In backend this is called GameState since state would empty this being a 'useState' which it isn't.
import type {GameBoardCellContent} from "./GameBoardCellContent.ts";
import type {GameStatus} from "./GameStatus.ts";


export type GameBoard={
    id:string;
    board: GameBoardCellContent[][],
    player1:string;
    player2:string;
    atTurn: boolean;
    status: GameStatus;
    winner:string;
};