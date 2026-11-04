import { describe, expect, it, vi,type Mock, beforeEach } from "vitest";
import { fireEvent, render, screen } from "@testing-library/react";
import { WelcomePage } from "../../src/pages/WelcomePage";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter, Route, Routes } from "react-router-dom";

const mockNavigate = vi.fn();
vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual<typeof import("react-router-dom")>(
        "react-router-dom"
    );
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});
vi.mock("../../src/hooks/useGetOngoingGameBoard.ts", () => ({
    useGetOngoingGameBoard: vi.fn(),
}));

import { useGetOngoingGameBoard } from "../../src/hooks/useGetOngoingGameBoard.ts";

describe("WelcomePage", () => {
    const renderPage = () => {
        const queryClient = new QueryClient();
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter initialEntries={["/"]}>
                    <Routes>
                        <Route path="/" element={<WelcomePage />} />
                    </Routes>
                </MemoryRouter>
            </QueryClientProvider>
        );
    };

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it("shows loading state while game is pending", () => {
        (useGetOngoingGameBoard as Mock).mockReturnValue({
            gameState: null,
            isGamePending: true,
        });

        renderPage();

        expect(screen.getByText("Loading...")).toBeInTheDocument();
    });

    it("shows welcome content initially", () => {
        (useGetOngoingGameBoard as Mock).mockReturnValue({
            gameState: null,
            isGamePending: false,
        });

        renderPage();

        expect(screen.getByText("Welcome to Go!")).toBeInTheDocument();
        expect(
            screen.getByRole("button", { name: /nieuw spel starten/i })
        ).toBeInTheDocument();
        expect(
            screen.queryByText("Select a board size")
        ).not.toBeInTheDocument();
    });

    it("shows SizeSelector after clicking Nieuw spel starten", () => {
        (useGetOngoingGameBoard as Mock).mockReturnValue({
            gameState: null,
            isGamePending: false,
        });

        renderPage();

        fireEvent.click(
            screen.getByRole("button", { name: /nieuw spel starten/i })
        );

        expect(screen.queryByText("Welcome to Go!")).not.toBeInTheDocument();
        expect(
            screen.getByText("Select a board size")
        ).toBeInTheDocument();
    });

    it("returns to welcome page when Back is clicked", () => {
        (useGetOngoingGameBoard as Mock).mockReturnValue({
            gameState: null,
            isGamePending: false,
        });

        renderPage();

        fireEvent.click(
            screen.getByRole("button", { name: /nieuw spel starten/i })
        );

        expect(screen.getByText("Select a board size")).toBeInTheDocument();

        fireEvent.click(screen.getByRole("button", { name: /back/i }));

        expect(screen.getByText("Welcome to Go!")).toBeInTheDocument();
        expect(
            screen.queryByText("Select a board size")
        ).not.toBeInTheDocument();
    });

    it("navigates automatically when there is an ongoing non-AI game", () => {
        (useGetOngoingGameBoard as Mock).mockReturnValue({
            isGamePending: false,
            gameState: {
                id: "123",
                isAiGame: false,
            },
        });

        renderPage();

        expect(mockNavigate).toHaveBeenCalledWith("/game/123");
    });

    it("shows 'Verder spelen' button for ongoing AI game", () => {
        (useGetOngoingGameBoard as Mock).mockReturnValue({
            isGamePending: false,
            gameState: {
                id: "456",
                isAiGame: true,
            },
        });

        renderPage();

        expect(
            screen.getByText("Je hebt nog een spel tegen een AI")
        ).toBeInTheDocument();

        fireEvent.click(
            screen.getByRole("button", { name: /verder spelen/i })
        );

        expect(mockNavigate).toHaveBeenCalledWith("/game/456");
    });
});
