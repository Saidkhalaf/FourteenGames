import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen} from "@testing-library/react";
import { usePlatformUsers } from "../../src/hooks/api/user/usePlatformUsers.tsx";
import { useTheme } from "@mui/material";
import {CurrentPlayersOverlay} from "../../src/components/overlay/CurrentPlayersOverlay.tsx";
import {MemoryRouter} from "react-router-dom";

vi.mock("../../src/hooks/api/user/usePlatformUsers.tsx", () => ({
    usePlatformUsers: vi.fn(),
}));

vi.mock("@mui/material", async () => {
    const actual = await vi.importActual("@mui/material");
    return {
        ...actual,
        useTheme: vi.fn(),
    };
});

describe("CurrentPlayersOverlay", () => {
    const mockPlayers = [
        { userId: "u1" },
        { userId: "u2" },
    ];

    const mockUserData = [
        { id: "u1", userName: "PlayerOne" },
        { id: "u2", userName: "PlayerTwo" },
    ];

    const mockTheme = {
        palette: {
            primary: {
                main: "#000000"
            }
        }
    };

    beforeEach(() => {
        vi.clearAllMocks();
        (usePlatformUsers as Mock).mockReturnValue({
            userData: mockUserData,
            isError: false
        });
        (useTheme as Mock).mockReturnValue(mockTheme);
    });

    it("renders the player list when data is loaded successfully", () => {
        //Arrange
        //Act
        render(
            <MemoryRouter>
                <CurrentPlayersOverlay players={mockPlayers} />
            </MemoryRouter>
        );
        //Assert
        expect(screen.getByText("Spelers in dit spel:")).toBeInTheDocument();
        expect(screen.getByText("PlayerOne")).toBeInTheDocument();
        expect(screen.getByText("PlayerTwo")).toBeInTheDocument();
    });

    it("renders the error state when fetching fails", () => {
        //Arrange
        (usePlatformUsers as Mock).mockReturnValue({
            userData: null,
            isError: true
        });
        //Act
        render(
            <MemoryRouter>
                <CurrentPlayersOverlay players={mockPlayers} />
            </MemoryRouter>
        );
        //Assert
        expect(screen.getByText("Er ging iets mis met het ophalen van de spelers...")).toBeInTheDocument();
        expect(screen.queryByText("Spelers in dit spel:")).not.toBeInTheDocument();
    });

    it("renders the error state when data is undefined", () => {
        //Arrange
        (usePlatformUsers as Mock).mockReturnValue({
            userData: undefined,
            isError: false
        });
        //Act
        render(
            <MemoryRouter>
                <CurrentPlayersOverlay players={mockPlayers} />
            </MemoryRouter>
        );
        //Assert
        expect(screen.getByText("Er ging iets mis met het ophalen van de spelers...")).toBeInTheDocument();
    });

});
