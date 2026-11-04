import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { UserPreferencePage } from "../../src/pages/UserPreferencePage";
import { useSecurityStore } from "../../src/stores/securityStore";
import { usePlatformUser } from "../../src/hooks/usePlatformUser.tsx";

vi.mock("../../src/stores/securityStore", () => ({
    useSecurityStore: vi.fn(),
}));

vi.mock("../../src/hooks/usePlatformUser.tsx", () => ({
    usePlatformUser: vi.fn(),
}));

vi.mock("../../src/components/controls/ThemeControls.tsx", () => ({
    ThemeControls: () => <div data-testid="theme-controls">ThemeControls</div>,
}));

function setSecurityStore(state: unknown) {
    (useSecurityStore as unknown as Mock).mockImplementation((selector: any) =>
        selector(state)
    );
}

describe("UserPreferencePage", () => {
    const mockLogin = vi.fn();
    const mockLogout = vi.fn();
    const mockManageAccount = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
        (usePlatformUser as Mock).mockReturnValue({
            platformUser: { profilePictureUrl: undefined },
        });
    });

    it("redirects to /games when not authenticated", () => {
        //Arrange
        setSecurityStore({
            isAuthenticated: () => false,
            isInitialised: true,
        });
        //Act
        render(
            <MemoryRouter>
                <UserPreferencePage />
            </MemoryRouter>
        );
        //Assert
        expect(screen.queryByText("Uw gegevens")).not.toBeInTheDocument();
    });

    it("renders logged-in user info", () => {
        //Arrange
        setSecurityStore({
            isAuthenticated: () => true,
            isInitialised: true,
            loggedInUser: { username: "testUser", firstName: "Test", lastName: "User", email: "test@example.com" },
            login: mockLogin,
            logout: mockLogout,
            manageAccount: mockManageAccount,
        });
        //Act
        render(
            <MemoryRouter>
                <UserPreferencePage />
            </MemoryRouter>
        );
        //Assert
        expect(screen.getByText("testUser")).toBeInTheDocument();
        expect(screen.getByText("Naam: Test User")).toBeInTheDocument();
        expect(screen.getByText("Email: test@example.com")).toBeInTheDocument();
        expect(screen.getByText("Uw voorkeuren")).toBeInTheDocument();
        expect(screen.getByTestId("theme-controls")).toBeInTheDocument();
    });

    it("calls manageAccount when 'inloggegevens wijzigen' button is clicked", () => {
        //Arrange
        setSecurityStore({
            isAuthenticated: () => true,
            isInitialised: true,
            loggedInUser: { username: "testUser" },
            login: mockLogin,
            logout: mockLogout,
            manageAccount: mockManageAccount,
        });
        //Act
        render(
            <MemoryRouter>
                <UserPreferencePage />
            </MemoryRouter>
        );
        //Assert
        fireEvent.click(screen.getByText("inloggegevens wijzigen"));
        expect(mockManageAccount).toHaveBeenCalled();
    });

    it("shows 'Uitloggen' button when authenticated and calls logout on click", () => {
        //Arrange
        setSecurityStore({
            isAuthenticated: () => true,
            isInitialised: true,
            loggedInUser: { username: "testUser" },
            login: mockLogin,
            logout: mockLogout,
            manageAccount: mockManageAccount,
        });
        //Act
        render(
            <MemoryRouter>
                <UserPreferencePage />
            </MemoryRouter>
        );
        //Assert
        const logoutButton = screen.getByText("Uitloggen");
        fireEvent.click(logoutButton);
        expect(mockLogout).toHaveBeenCalled();
    });
    it("links to the user's profile page", () => {
        //Arrange
        setSecurityStore({
            isAuthenticated: () => true,
            isInitialised: true,
            loggedInUser: { username: "testUser" },
            login: mockLogin,
            logout: mockLogout,
            manageAccount: mockManageAccount,
        });
        //Act
        render(
            <MemoryRouter>
                <UserPreferencePage />
            </MemoryRouter>
        );
        //Assert
        const profileLink = screen.getByText("Profiel bekijken");
        expect(profileLink.closest("a")).toHaveAttribute("href", "/profile/testUser");
    });
});
