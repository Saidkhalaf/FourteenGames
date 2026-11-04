import {render, screen} from "@testing-library/react";
import {describe, it, expect, vi, beforeEach} from "vitest";
import {useLobbyList} from "../../src/hooks/api/lobby/useLobbyList.tsx";
import {useGamesList} from "../../src/hooks/api/games/useGamesList.tsx";
import {useSelectionStore} from "../../src/stores/selectionStore.ts";
import {usePlatformUser} from "../../src/hooks/usePlatformUser.tsx";
import {LobbyPage} from "../../src/pages/LobbyPage.tsx";

vi.mock("../../src/hooks/api/lobby/useLobbyList.tsx");
vi.mock("../../src/hooks/api/games/useGamesList.tsx");
vi.mock("../../src/stores/selectionStore.ts");
vi.mock("../../src/hooks/usePlatformUser.tsx");

vi.mock("../../src/components/tables/LobbyTable.tsx", () => ({
    LobbyTable: ({lobbies, games}: any) => (
        <div data-testid="lobby-table">
            <span data-testid="lobbies-count">{lobbies.length}</span>
            <span data-testid="games-count">{games.length}</span>
        </div>
    )
}));

describe("LobbyPage", () => {
    const mockGames = [
        { id: "game1", name: "Game 1" },
        { id: "game2", name: "Game 2" },
        { id: "game3", name: "Game 3" },
    ];

    const mockLobbies = [
        { id: "1", gameId: "game1" },
        { id: "2", gameId: "game2" },
    ];

    const mockSetSelectedGameId = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();

        vi.mocked(useLobbyList).mockReturnValue({ lobbies: mockLobbies } as any);
        vi.mocked(useGamesList).mockReturnValue({ games: mockGames } as any);

        // 👇 Mock platform user with owned copies for all games
        vi.mocked(usePlatformUser).mockReturnValue({
            platformUser: {
                ownedCopies: mockGames.map(g => ({ gameId: g.id }))
            }
        } as any);

        // 👇 Proper selector-based store mock
        vi.mocked(useSelectionStore).mockImplementation((selector: any) =>
            selector({
                selectedGameId: null,
                setSelectedGameId: mockSetSelectedGameId
            })
        );
    });

    it("renders lobby page with title", () => {
        //Arrange
        //Act
        render(<LobbyPage />);
        //Assert
        expect(screen.getByRole("heading", { name: /lobbies/i })).toBeInTheDocument();
    });

    it("displays all games when no game is selected", () => {
        //Arrange
        //Act
        render(<LobbyPage />);
        //Assert
        expect(screen.getByTestId("games-count")).toHaveTextContent("3");
    });

    it("filters games when a game is selected", () => {
        //Arrange
        vi.mocked(useSelectionStore).mockImplementation((selector: any) =>
            selector({
                selectedGameId: "game1",
                setSelectedGameId: mockSetSelectedGameId
            })
        );
        //Act
        render(<LobbyPage />);
        //Assert
        expect(screen.getByTestId("games-count")).toHaveTextContent("1");
    });

    it("shows 'Alles' option in game select dropdown", () => {
        //Arrange
        //Act
        render(<LobbyPage />);
        //Assert
        expect(screen.getByText("Alles")).toBeInTheDocument();
    });

    it("passes lobbies to LobbyTable", () => {
        //Arrange
        //Act
        render(<LobbyPage />);
        //Assert
        expect(screen.getByTestId("lobbies-count")).toHaveTextContent("2");
    });

    it("renders options card with correct title", () => {
        //Arrange
        //Act
        render(<LobbyPage />);
        //Assert
        expect(screen.getByRole("heading", { name: /opties/i })).toBeInTheDocument();
    });
});
