export type GameState = {
    id: string;
    board: string[][];
    size: number;
    winner: string;
    score: number;
    player1Id: string;
    player2Id: string;
    isPlayer1AtTurn: boolean;
    isLastTurnPassed: boolean;
    isAiGame:boolean;
}