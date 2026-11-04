import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { GameStorePage } from "../../src/pages/GameStorePage";
import { useGamesList } from "../../src/hooks/api/games/useGamesList.tsx";
import { useOwnedGames } from "../../src/hooks/api/games/useOwnedGames.tsx";
import { usePlatformUser } from "../../src/hooks/usePlatformUser.tsx";

vi.mock("../../src/hooks/api/games/useGamesList.tsx", () => ({
    useGamesList: vi.fn(),
}));

vi.mock("../../src/hooks/api/games/useOwnedGames.tsx", () => ({
    useOwnedGames: vi.fn(),
}));

vi.mock("../../src/hooks/usePlatformUser.tsx", () => ({
    usePlatformUser: vi.fn(),
}));

vi.mock("../../src/components/lists/StoreGameCardList.tsx", () => ({
    StoreGameCardList: ({ games }: { games: any[] }) => (
        <div data-testid="game-card-list">{games.map(g => <div key={g.id}>{g.name}</div>)}</div>
    ),
}));

vi.mock("../../src/components/dialogs/AddCreditsDialog.tsx", () => ({
    AddCreditsDialog: ({ isOpen }: { isOpen: boolean }) => (
        <div data-testid="add-credits-dialog">{isOpen ? "Dialog open" : "Dialog closed"}</div>
    ),
}));

describe("GameStorePage", () => {
    const mockGames = [
        { id: 1, name: "Alpha Game" },
        { id: 2, name: "Beta Game" },
    ];
    const mockOwnedGames = [
        { gameId: 2, name: "Beta Game" },
    ];
    const mockPlatformUser = { credits: 100 };

    beforeEach(() => {
        vi.clearAllMocks();
        (useGamesList as Mock).mockReturnValue({ games: mockGames });
        (useOwnedGames as Mock).mockReturnValue({ ownedGames: mockOwnedGames });
        (usePlatformUser as Mock).mockReturnValue({ platformUser: mockPlatformUser });
    });

    it("renders the store page with user credits and games", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <GameStorePage />
            </MemoryRouter>
        );
        //Assert
        screen.debug();
        expect(screen.getByText("Store")).toBeInTheDocument();
        expect(screen.getByTestId("SaldoVisual")).toBeInTheDocument();
        expect(screen.getByText("Alpha Game")).toBeInTheDocument();
        expect(screen.queryByText("Beta Game")).not.toBeInTheDocument();
    });

    it("toggles sort order when abc button is clicked", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <GameStorePage />
            </MemoryRouter>
        );
        //Assert
        const sortButton = screen.getByText("abc");
        fireEvent.click(sortButton);
        expect(sortButton).toBeInTheDocument();
    });

    it("filters games based on search term", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <GameStorePage />
            </MemoryRouter>
        );
        //Assert
        const searchInput = screen.getByPlaceholderText("Search…");
        fireEvent.change(searchInput, { target: { value: "alpha" } });
        expect(screen.getByText("Alpha Game")).toBeInTheDocument();
        fireEvent.change(searchInput, { target: { value: "nonexistent" } });
        expect(screen.queryByText("Alpha Game")).not.toBeInTheDocument();
        expect(screen.getByText("Geen games gevonden")).toBeInTheDocument();
    });

    it("opens and closes AddCreditsDialog when button is clicked", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <GameStorePage />
            </MemoryRouter>
        );
        //Assert
        const addCreditsButton = screen.getByText("Saldo toevoegen");
        expect(screen.getByTestId("add-credits-dialog")).toHaveTextContent("Dialog closed");
        fireEvent.click(addCreditsButton);
        expect(screen.getByTestId("add-credits-dialog")).toHaveTextContent("Dialog open");
    });
});
