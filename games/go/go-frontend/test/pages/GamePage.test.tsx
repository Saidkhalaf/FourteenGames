import {beforeEach, describe, expect, it, vi} from "vitest";
import {render, screen, waitFor} from "@testing-library/react";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {MemoryRouter, Route, Routes} from "react-router-dom";
import {testBoard} from "../data/testGameBoardStates";
import {GamePage} from "../../src/pages/GamePage";
import "@testing-library/jest-dom/vitest";

const mockUseStartNewGame = vi.fn();
const mockUseGameBoard = vi.fn();
vi.mock("../../src/hooks/useStartNewGame.ts", () => {
    return {
        useStartNewGame: () => mockUseStartNewGame(),
    };
});
vi.mock("../../src/hooks/useGameBoard.ts", () => {
    return {
        useGameBoard: () => mockUseGameBoard(),
    };
});

describe("GamePage", () => {
    beforeEach(() => {
        // arrange
        mockUseStartNewGame.mockReturnValue({
            createGame: vi.fn(),
            newGame: testBoard,
            isPending: false,
            isError: false,
        });
        mockUseGameBoard.mockReturnValue({
            gameState: testBoard,
            isGamePending: false,
            isError: false,
        });
    });

    it("Correctly renders page when a %ix%i grid is provided.", async () => {
        // Arrange
        const queryClient = new QueryClient();

        mockUseStartNewGame.mockReturnValueOnce({
            createGame: vi.fn(),
            newGame: testBoard,
            isPending: false,
            isError: false,
        });
        mockUseGameBoard.mockReturnValueOnce({
            gameState: testBoard,
            isGamePending: false,
            isError: false,
        });
        const gameId = "5";
        // Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter initialEntries={[`/game/${gameId}`]}>
                    <Routes>
                        <Route path="/game/:id" element={<GamePage />} />
                    </Routes>
                </MemoryRouter>
            </QueryClientProvider>
        );
        // Assert
        await waitFor(() => {
            const pieces = screen.getAllByTestId("go-game-board-piece");
            expect(pieces.length).toBe(9 * 9);
        });
    });

});