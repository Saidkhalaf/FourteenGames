import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { AchievementPage } from "../../src/pages/AchievementPage";
import { useGamesList } from "../../src/hooks/api/games/useGamesList.tsx";
import { useOwnedGames } from "../../src/hooks/api/games/useOwnedGames.tsx";

vi.mock("../../src/hooks/api/games/useGamesList.tsx", () => ({
    useGamesList: vi.fn(),
}));

vi.mock("../../src/hooks/api/games/useOwnedGames.tsx", () => ({
    useOwnedGames: vi.fn(),
}));

vi.mock("../../src/components/GameFullAchievementList.tsx", () => ({
    GameFullAchievementList: ({ gameName, achievements }: { gameName: string, achievements: any[] }) => (
        <div data-testid="game-achievement-list">
            <div>{gameName}</div>
            {achievements.map(a => <div key={a.id}>{a.name}</div>)}
        </div>
    ),
}));

describe("AchievementPage", () => {
    const mockGames = [
        { id: 1, name: "Alpha Game", achievements: [{ id: "a1", name: "Achieve 1" }] },
        { id: 2, name: "Beta Game", achievements: [] },
        { id: 3, name: "Gamma Game", achievements: [{ id: "g1", name: "Gamma Achieve" }] },
    ];

    const mockOwnedGames = [
        { gameId: 1 },
        { gameId: 3 },
    ];

    beforeEach(() => {
        vi.clearAllMocks();
        (useGamesList as Mock).mockReturnValue({ games: mockGames });
        (useOwnedGames as Mock).mockReturnValue({ ownedGames: mockOwnedGames });
    });

    it("renders the page title", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <AchievementPage />
            </MemoryRouter>
        );
        //Assert
        expect(screen.getByText("Achievements")).toBeInTheDocument();
    });

    it("renders achievements only for owned games", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <AchievementPage />
            </MemoryRouter>
        );
        //Assert
        expect(screen.getByText("Alpha Game")).toBeInTheDocument();
        expect(screen.getByText("Achieve 1")).toBeInTheDocument();
        expect(screen.getByText("Gamma Game")).toBeInTheDocument();
        expect(screen.getByText("Gamma Achieve")).toBeInTheDocument();
        expect(screen.queryByText("Beta Game")).not.toBeInTheDocument();
    });

    it("filters out games with no achievements", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <AchievementPage />
            </MemoryRouter>
        );
        //Assert
        expect(screen.queryByText("Beta Game")).not.toBeInTheDocument();
    });

    it("renders multiple GameFullAchievementList components", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <AchievementPage />
            </MemoryRouter>
        );
        //Assert
        const lists = screen.getAllByTestId("game-achievement-list");
        expect(lists).toHaveLength(2);
    });
});
