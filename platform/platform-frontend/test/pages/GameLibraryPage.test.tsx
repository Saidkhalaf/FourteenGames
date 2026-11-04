import {describe, it, expect, vi, beforeEach, type Mock} from "vitest";
import {render, screen, fireEvent} from "@testing-library/react";
import {MemoryRouter} from "react-router-dom";
import {GameLibraryPage} from "../../src/pages/GameLibraryPage";
import {useGamesList} from "../../src/hooks/api/games/useGamesList.tsx";
import {useOwnedGames} from "../../src/hooks/api/games/useOwnedGames.tsx";
import {useFavoriteGames} from "../../src/hooks/api/favoriteGames/useFavoriteGames.tsx";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

vi.mock("../../src/hooks/api/games/useGamesList.tsx", () => ({
    useGamesList: vi.fn(),
}));

vi.mock("../../src/hooks/api/games/useOwnedGames.tsx", () => ({
    useOwnedGames: vi.fn(),
}));

vi.mock("../../src/hooks/api/favoriteGames/useFavoriteGames.tsx", () => ({
    useFavoriteGames: vi.fn(),
}));

vi.mock("../../src/components/lists/GameCardList.tsx", () => ({
    GameCardList: ({games}: { games: any[] }) => (
        <div data-testid="game-card-list">{games.map(g => <div key={g.id}>{g.name}</div>)}</div>
    ),
}));

vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual<any>("react-router-dom");
    return {
        ...actual,
        useNavigate: () => vi.fn(),
    };
});

describe("GameLibraryPage", () => {
    const mockGames = [
        {id: 1, name: "Alpha Game"},
        {id: 2, name: "Beta Game"},
    ];
    const mockOwnedGames = [
        {gameId: 1},
    ];
    const mockFavorites = [
        {gameId: 1},
    ];

    beforeEach(() => {
        vi.clearAllMocks();
        (useGamesList as Mock).mockReturnValue({games: mockGames});
        (useOwnedGames as Mock).mockReturnValue({ownedGames: mockOwnedGames});
        (useFavoriteGames as Mock).mockReturnValue({favorites: mockFavorites});
    });

    it("renders only owned games", () => {
        //Arrange
        const queryClient = new QueryClient();
        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <GameLibraryPage/>
                </MemoryRouter>
            </QueryClientProvider>
        );
        //Assert
        expect(screen.getByText("Games")).toBeInTheDocument();
        expect(screen.getByText("Alpha Game")).toBeInTheDocument();
        expect(screen.queryByText("Beta Game")).not.toBeInTheDocument();
    });

    it("toggles sort order when abc button is clicked", () => {
        //Arrange
        const queryClient = new QueryClient();
        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <GameLibraryPage/>
                </MemoryRouter>
            </QueryClientProvider>
        )
        //Assert
        const sortButton = screen.getByText("abc");
        fireEvent.click(sortButton);
        expect(sortButton).toBeInTheDocument();
    });

    it("filters owned games based on search term", () => {
        //Arrange
        const queryClient = new QueryClient();
        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <GameLibraryPage/>
                </MemoryRouter>
            </QueryClientProvider>
        );
        //Assert
        const searchInput = screen.getByPlaceholderText("Search…");
        fireEvent.change(searchInput, {target: {value: "alpha"}});
        expect(screen.getByText("Alpha Game")).toBeInTheDocument();
        fireEvent.change(searchInput, {target: {value: "beta"}});
        expect(screen.queryByText("Alpha Game")).not.toBeInTheDocument();
        expect(screen.getByText("Het lijkt erop dat je nog geen games hebt...")).toBeInTheDocument();
    });

    it("shows no games message when no owned games", () => {
        //Arrange
        const queryClient = new QueryClient();
        (useOwnedGames as Mock).mockReturnValue({ownedGames: []});
        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <GameLibraryPage/>
                </MemoryRouter>
            </QueryClientProvider>
        );
        //Assert
        expect(screen.getByText("Het lijkt erop dat je nog geen games hebt...")).toBeInTheDocument();
        expect(screen.getByText("Winkel pagina")).toBeInTheDocument();
    });
});
