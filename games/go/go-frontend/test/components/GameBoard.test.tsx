import {describe, expect, it, vi, beforeEach} from "vitest";
import {render, screen} from "@testing-library/react";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import {GameBoard} from "../../src/components/gameboard/GameBoard";
import {testBoard, testBoardLarge, testBoardMedium, whiteWinsBoard, testBoardOpponentPassed} from "../data/testGameBoardStates";
import "@testing-library/jest-dom/vitest";

// Mocks for hooks used inside GameBoard to prevent side effects
const mockUsePlaceStone = vi.fn();
const mockUseAiMove = vi.fn();
const mockUsePassTurn = vi.fn();
const mockUseStartNewGame = vi.fn();

vi.mock("../../src/hooks/usePlaceStone.ts", () => ({
  usePlaceStone: () => mockUsePlaceStone(),
}));

vi.mock("../../src/hooks/useAiMove.ts", () => ({
  useAiMove: () => mockUseAiMove(),
}));

vi.mock("../../src/hooks/usePassTurn.ts", () => ({
  usePassTurn: () => mockUsePassTurn(),
}));

vi.mock("../../src/hooks/useStartNewGame.ts", () => ({
  useStartNewGame: () => mockUseStartNewGame(),
}));

describe("GameBoard UI states", () => {
  beforeEach(() => {
    // Arrange
    mockUsePlaceStone.mockReturnValue({ requestMove: vi.fn(), isPending: false });
    mockUseAiMove.mockReturnValue({ requestAiMove: vi.fn(), isPending: false });
    mockUsePassTurn.mockReturnValue({ requestPassTurn: vi.fn(), isPending: false });
    mockUseStartNewGame.mockReturnValue({ createGame: vi.fn(), isPending: false });
  });

  const renderWithClient = (ui: React.ReactNode) => {
    // Arrange
    const client = new QueryClient();
    // Act
    return render(
      <MemoryRouter>
        <QueryClientProvider client={client}>{ui}</QueryClientProvider>
      </MemoryRouter>
    );
  };

  it("shows 'turn' when atTurn is true", () => {
    // Act
    renderWithClient(<GameBoard board={testBoard} />);
    // Assert
    expect(screen.getByText(/turn/i)).toBeInTheDocument();
  });

  it("shows 'Opponent passed their turn' when isLastTurnPassed is true", () => {
    // Act
    renderWithClient(<GameBoard board={testBoardOpponentPassed} />);

    // Assert
    expect(screen.getByText(/opponent passed their turn/i)).toBeInTheDocument();
  });

  it("shows 'AI is thinking…' when atTurn is false and no winner", () => {
    // Act
    renderWithClient(<GameBoard board={testBoardLarge} />);
    screen.debug();
    // Assert
    expect(screen.getByText(/AI is thinking/i)).toBeInTheDocument();
  });

  it("shows winner card with BLACK when black has won", () => {
    // Act
    renderWithClient(<GameBoard board={testBoardMedium} />);

    // Assert
    expect(screen.getByText(/winner/i)).toBeInTheDocument();
    expect(screen.getByText(/BLACK/i)).toBeInTheDocument();
    expect(screen.getByText(/Score: 99/i)).toBeInTheDocument();
  });

  it("shows winner card with WHITE when white has won (black losing)", () => {
    // Act
    renderWithClient(<GameBoard board={whiteWinsBoard} />);

    // Assert
    expect(screen.getByText(/winner/i)).toBeInTheDocument();
    expect(screen.getByText(/WHITE/i)).toBeInTheDocument();
    expect(screen.getByText(/Score: 42/i)).toBeInTheDocument();
  });
});
