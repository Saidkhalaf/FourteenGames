const express = require('express');
const app = express();
const port = 8000;

app.use(express.json());
app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

const EMPTY = "_";
const BLACK = "B";
const WHITE = "W";

const isValid = (r, c, size) => r >= 0 && r < size && c >= 0 && c < size;

app.post('/api/go/ai-move', (req, res) => {
    if (req.body.isLastTurnPassed) {
        console.log("AI passes");
        return res.json({row: -1, col: -1, message: "AI passes"});
    }

    const boardState = req.body.boardState;
    const size = req.body.boardState.length;

    if (!boardState || !Array.isArray(boardState) || boardState.length === 0) {
        return res.status(400).json({ error: "Invalid boardState format" });
    }

    const center = Math.floor(size / 2);

    let possibleMoves = [];

    for (let r = 0; r < size; r++) {
        for (let c = 0; c < size; c++) {

            if (boardState[r][c] === EMPTY) {

                let score = 0;
                let occupiedNeighbors = 0;

                score += Math.random() * 5;

                const distFromCenter = Math.abs(r - center) + Math.abs(c - center);
                score += (size - distFromCenter) * 0.5;

                const directions = [
                    { dr: -1, dc: 0 }, { dr: 1, dc: 0 },
                    { dr: 0, dc: -1 }, { dr: 0, dc: 1 }
                ];

                let blackNeighbors = 0;

                directions.forEach(({dr, dc}) => {
                    const nr = r + dr;
                    const nc = c + dc;

                    if (isValid(nr, nc, size)) {
                        const neighbor = boardState[nr][nc];
                        if (neighbor === WHITE) {
                            score += 3;
                            occupiedNeighbors++;
                        } else if (neighbor === BLACK) {
                            score += 2.5;
                            occupiedNeighbors++;
                            blackNeighbors++;
                        }
                    }
                });

                if (blackNeighbors >= 3) {
                    continue;
                }

                if (occupiedNeighbors === 4) {
                    score -= 50;
                }

                possibleMoves.push({ row: r, col: c, score: score });
            }
        }
    }

    if (possibleMoves.length === 0) {
        console.log("AI passes");
        return res.json({row: -1, col: -1, message: "AI passes"});
    }

    possibleMoves.sort((a, b) => b.score - a.score);

    const topChoices = possibleMoves.slice(0, 3);
    const selectedMove = topChoices[Math.floor(Math.random() * topChoices.length)];

    const flatIndex = (selectedMove.row * size) + selectedMove.col;

    const response = {
        row: selectedMove.row,
        col: selectedMove.col,
        flatIndex: flatIndex,
        passed: false
    };

    console.log(`AI places White at [${selectedMove.row}, ${selectedMove.col}] (Score: ${selectedMove.score.toFixed(2)})`);

    setTimeout(() => {
        res.json(response);
    }, 200 + Math.random() * 200);
});

app.listen(port, () => {
    console.log(`Mock Go AI Server running at http://localhost:${port}`);
    console.log(`Endpoint: POST /api/go/ai-move`);
});