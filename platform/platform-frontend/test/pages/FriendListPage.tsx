import {describe, expect, it, type Mocked, vi} from "vitest";
import {render, screen, waitFor} from "@testing-library/react";
import {MemoryRouter, Route, Routes} from "react-router-dom";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {emptyFriendList, emptyFriendRequestList,testFriendList, testFriendRequestList} from "../data/TestUserData.ts";
import axios, {type AxiosStatic} from "axios";
import {FriendListPage} from "../../src/pages/FriendListPage.tsx";

vi.mock('axios')

describe('FriendListPage', () => {
    it("renders friend list page when empty friendrequests are shown and a filled friendlist is provided", async () => {
        //Arrange
        const queryClient = new QueryClient();
        const searchedUser = "anotherUser";
        const mockedAxios = axios as unknown as Mocked<AxiosStatic>;
        mockedAxios.get.mockResolvedValueOnce({data: testFriendList});
        mockedAxios.get.mockResolvedValueOnce({data: emptyFriendRequestList});

        //Act
        render(
            <QueryClientProvider client={queryClient}>
                    <MemoryRouter initialEntries={[`/profile/${searchedUser}`]}>
                        <Routes>
                            <Route path="/profile/:userName"
                                   element={<FriendListPage/>}/>
                        </Routes>
                    </MemoryRouter>
            </QueryClientProvider>
        );

        //Assert
        await waitFor(() => {
            expect(screen.queryByTestId("friend-request-card")).not.toBeInTheDocument();
            expect(screen.getByTestId("friend-card")).toBeInTheDocument();
        });
    });

    it("renders friend list page when filled friendrequests are shown and a empty friendlist is provided", async () => {
        //Arrange
        const queryClient = new QueryClient();
        const searchedUser = "anotherUser";
        const mockedAxios = axios as unknown as Mocked<AxiosStatic>;
        mockedAxios.get.mockResolvedValueOnce({data: emptyFriendList});
        mockedAxios.get.mockResolvedValueOnce({data: testFriendRequestList});

        //Act
        render(
            <QueryClientProvider client={queryClient}>
                    <MemoryRouter initialEntries={[`/profile/${searchedUser}`]}>
                        <Routes>
                            <Route path="/profile/:userName"
                                   element={<FriendListPage/>}/>
                        </Routes>
                    </MemoryRouter>
            </QueryClientProvider>
        );

        //Assert
        await waitFor(() => {
            expect(screen.queryByTestId("friend-request-card")).toBeInTheDocument();
            expect(screen.getByTestId("friend-card")).not.toBeInTheDocument();
        });
    });

    it("correct page renders when a user has no friends", async () => {
        //Arrange
        const queryClient = new QueryClient();
        const searchedUser = "anotherUser";
        const mockedAxios = axios as unknown as Mocked<AxiosStatic>;
        mockedAxios.get.mockResolvedValueOnce({data: emptyFriendList});
        mockedAxios.get.mockResolvedValueOnce({data: emptyFriendRequestList});

        //Act
        render(
            <QueryClientProvider client={queryClient}>
                    <MemoryRouter initialEntries={[`/profile/${searchedUser}`]}>
                        <Routes>
                            <Route path="/profile/:userName"
                                   element={<FriendListPage/>}/>
                        </Routes>
                    </MemoryRouter>
            </QueryClientProvider>
        );

        //Assert
        await waitFor(() => {
            expect(screen.queryByTestId("friend-request-card")).not.toBeInTheDocument();
            expect(screen.getByTestId("friend-card")).not.toBeInTheDocument();
            expect(screen.getByTestId("no-friend-warning")).toBeInTheDocument();
        });
    });

})