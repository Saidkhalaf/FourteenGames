import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { ProfilePage } from "../../src/pages/ProfilePage";
import { useSecurityStore } from "../../src/stores/securityStore";
vi.mock("../../src/stores/securityStore", () => ({
    useSecurityStore: vi.fn(),
}));

vi.mock("../../src/hooks/usePlatformUser.tsx", () => ({
    usePlatformUser: vi.fn().mockReturnValue({
        platformUser: {
            bannerUrl: undefined,
            profilePictureUrl: undefined,
            biography: "Dit is een bio",
        },
    }),
}));

vi.mock("../../src/hooks/api/user/useUpdateUserProfile.tsx", () => ({
    useUpdateUserProfile: vi.fn().mockReturnValue({
        updateUserProfileData: vi.fn(),
    }),
}));
vi.mock("../../src/components/dialogs/UpdateProfileDialog.tsx", () => ({
    UpdateRoomDialog: ({ isOpen }: { isOpen: boolean }) => (
        <div data-testid="update-profile-dialog">
            {isOpen ? "Dialog open" : "Dialog closed"}
        </div>
    ),
}));

function setSecurityStore(state: any) {
    (useSecurityStore as unknown as Mock).mockImplementation((selector: any) =>
        selector(state),
    );
}

describe("ProfilePage", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    function renderWithRoute(initialPath: string) {
        return render(
            <MemoryRouter initialEntries={[initialPath]}>
                <Routes>
                    <Route path="/profile/:userName" element={<ProfilePage />} />
                    <Route path="/friends" element={<div>Friends page</div>} />
                    <Route path="/userPreferences" element={<div>User preferences page</div>} />
                </Routes>
            </MemoryRouter>,
        );
    }

    it("renders profile information for the route userName", () => {
        //Arrange
        setSecurityStore({
            loggedInUser: { username: "otherUser" },
        });
        //Act
        renderWithRoute("/profile/testUser");
        //Assert
        expect(screen.getByText("testUser")).toBeInTheDocument();
        expect(screen.getByText("Dit is een bio")).toBeInTheDocument();
    });

    it("does not show edit and friends buttons when viewing another user's profile", () => {
        //Arrange
        setSecurityStore({
            loggedInUser: { username: "otherUser" },
        });
        //Act
        renderWithRoute("/profile/testUser");
        //Assert
        expect(
            screen.queryByTestId("preference-edit-button"),
        ).not.toBeInTheDocument();
        expect(
            screen.queryByTestId("friend-view-button"),
        ).not.toBeInTheDocument();
    });

    it("shows edit buttons and friends button when viewing own profile", () => {
        //Arrange
        setSecurityStore({
            loggedInUser: { username: "testUser" },
        });
        //Act
        renderWithRoute("/profile/testUser");
        //Assert
        const editButtons = screen.getAllByTestId("preference-edit-button");
        expect(editButtons).toHaveLength(2);
        expect(screen.getByTestId("friend-view-button")).toBeInTheDocument();
    });

    it("navigates to friends page when friends button is clicked", () => {
        //Ararnge
        setSecurityStore({
            loggedInUser: { username: "testUser" },
        });
        //Act
        renderWithRoute("/profile/testUser");
        //Assert
        fireEvent.click(screen.getByTestId("friend-view-button"));
        expect(screen.getByText("Friends page")).toBeInTheDocument();
    });

    it("navigates to user preferences page when voorkeuren bewerken is clicked", () => {
        //Arrange
        setSecurityStore({
            loggedInUser: { username: "testUser" },
        });
        //Act
        renderWithRoute("/profile/testUser");
        //Assert
        const editButtons = screen.getAllByTestId("preference-edit-button");
        fireEvent.click(editButtons[0]);
        expect(screen.getByText("User preferences page")).toBeInTheDocument();
    });

    it("opens and closes the update profile dialog when profiel wijzigen is clicked", () => {
        //Arrange
        setSecurityStore({
            loggedInUser: { username: "testUser" },
        });
        //Act
        renderWithRoute("/profile/testUser");
        //Assert
        const editButtons = screen.getAllByTestId("preference-edit-button");
        const profileEditButton = editButtons[1];
        expect(screen.getByTestId("update-profile-dialog")).toHaveTextContent(
            "Dialog closed",
        );
        fireEvent.click(profileEditButton);
        expect(screen.getByTestId("update-profile-dialog")).toHaveTextContent(
            "Dialog open",
        );
    });
});
