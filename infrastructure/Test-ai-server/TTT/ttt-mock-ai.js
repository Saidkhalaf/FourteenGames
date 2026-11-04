const express = require('express');
const app = express();
const port = 8000;

// Middleware to parse JSON bodies
app.use(express.json());

// Middleware to handle CORS (allows your frontend to talk to this server)
app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

// The Mock AI Endpoint
app.post('/tic-tac-toe/api/ai-move', (req, res) => {
    console.log('Request received:', req.body);

    const { boardState } = req.body;

    // 1. Find all empty spots (where value is 0)
    let availableMoves = [];

    if (!boardState || !Array.isArray(boardState)) {
        return res.status(400).json({ error: "Invalid boardState format" });
    }

    for (let rowIndex = 0; rowIndex < 3; rowIndex++) {
        for (let colIndex = 0; colIndex < 3; colIndex++) {
            if (boardState[rowIndex][colIndex] === '0') {
                availableMoves.push({ row: rowIndex, col: colIndex });
            }
        }
    }

    // 2. Handle Game Over (No moves left)
    if (availableMoves.length === 0) {
        return res.status(400).json({ error: "No moves available (Board full)" });
    }

    // 3. Pick a random valid move
    const randomMove = availableMoves[Math.floor(Math.random() * availableMoves.length)];

    // 4. Calculate the "best_move" flat index (0-8)
    // Formula: (Row * 3) + Col
    const flatIndex = (randomMove.row * 3) + randomMove.col;

    const response = {
        best_move: flatIndex,
        row: randomMove.row,
        col: randomMove.col
    };

    console.log('Sending response:', response);

    // Simulate a slight delay (optional, makes it feel like AI is "thinking")
    setTimeout(() => {
        res.json(response);
    },100 + Math.random() * 300);
});

app.listen(port, () => {
    console.log(`Mock AI Server running at http://localhost:${port}`);
    console.log(`Endpoint: POST /api/tictactoe/ai-move`);
});