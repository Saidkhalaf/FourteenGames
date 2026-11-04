import {render, screen} from "@testing-library/react";
import {describe, it, expect, vi, beforeEach} from "vitest";
import {useParams, useNavigate} from "react-router-dom";
import {LobbyManagementPage} from "../../src/pages/LobbyManagementPage.tsx";
import {useLobby} from "../../src/hooks/api/lobby/useLobby.tsx";

// Mock dependencies
vi.mock("react-router-dom");
vi.mock("../../src/hooks/api/lobby/useLobby.tsx");
vi.mock("../../src/components/lists/LobbyMemberList.tsx", () => ({
    LobbyMemberList: ({lobby}: any) => (
        <div data-testid="lobby-member-list">Member List: {lobby.players.length}</div>
    )
}));
vi.mock("../../src/components/LobbyGameSettings.tsx", () => ({
    LobbyGameSettings: ({gameId}: any) => (
        <div data-testid="lobby-game-settings">Settings for {gameId}</div>
    )
}));
vi.mock("../../src/components/dialogs/ConfirmationDialog.tsx", () => ({
    ConfirmationDialog: ({isOpen, onAccept, onReject, confirmationMessage, rejectButtonContent}: any) => isOpen ? (
        <div data-testid="confirmation-dialog">
            <p>{confirmationMessage}</p>
            <button onClick={onAccept}>Accept</button>
            <button onClick={onReject}>{rejectButtonContent}</button>
        </div>
    ) : null
}));

describe("LobbyManagementPage", () => {
    const mockNavigate = vi.fn();
    const mockLobby = {
        id: "lobby123",
        gameId: "game456",
        currentGameSessionId: null,
        lobbyHostId: "host1",
        players: ["player1", "player2"],
        creationDate: new Date("2024-01-01T10:00:00.000Z"),
        maxPlayers: 10
    };

    beforeEach(() => {
        vi.clearAllMocks();
        vi.mocked(useParams).mockReturnValue({lobbyId: "lobby123"});
        vi.mocked(useNavigate).mockReturnValue(mockNavigate);
        vi.mocked(useLobby).mockReturnValue({lobby: mockLobby} as any);
    });

    it("renders lobby management page with correct details", () => {
        //arrange
        vi.mocked(useLobby).mockReturnValue({lobby: mockLobby} as any);
        //act
        render(<LobbyManagementPage />);
        //assert
        expect(screen.getByRole("heading", {name: /lobby beheer/i})).toBeInTheDocument();
        expect(screen.getByText(/lobbyid: lobby123/i)).toBeInTheDocument();
        expect(screen.getByText(/2\/10 spelers/i)).toBeInTheDocument();
    });

    it("renders member list and game settings components", () => {
        //arrange
        vi.mocked(useLobby).mockReturnValue({lobby: mockLobby} as any);
        //act
        render(<LobbyManagementPage />);
        //assert
        expect(screen.getByTestId("lobby-member-list")).toBeInTheDocument();
        expect(screen.getByTestId("lobby-game-settings")).toHaveTextContent("Settings for game456");
    });

    it("shows confirmation dialog when game session is started", () => {
        //arrange
        const lobbyWithSession = {...mockLobby, currentGameSessionId: "session1"};
        vi.mocked(useLobby).mockReturnValue({lobby: lobbyWithSession} as any);
        //act
        render(<LobbyManagementPage />);
        //assert
        expect(screen.getByTestId("confirmation-dialog")).toBeInTheDocument();
        expect(screen.getByText(/de lobby host heeft het spel gestart/i)).toBeInTheDocument();
    });
});
