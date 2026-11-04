import {describe, expect, it, type Mocked, vi} from "vitest";
import {render, screen, waitFor} from "@testing-library/react";
import {MemoryRouter, Route, Routes} from "react-router-dom";
import {GamePage} from "../../src/pages/GamePage";
import axios, {type AxiosStatic} from "axios";
import {compactGame1} from "../data/TestGames.ts";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {ErrorBoundary} from "react-error-boundary";

vi.mock('axios')

describe('GamePage', () => {
    it("throws error when fetched without a valid gameId", async () => {
        //Arrange
        const gameId = "6789";//Invalid ID.
        const queryClient = new QueryClient({
            defaultOptions: { queries: { retry: false } }
        });

        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter initialEntries={[`/games/${gameId}`]}>
                    <Routes>
                        <Route path="/games/:gameId" element={
                            <ErrorBoundary fallback={<div>Error!</div>}>
                                <GamePage/>
                            </ErrorBoundary>
                        }/>
                    </Routes>
                </MemoryRouter>
            </QueryClientProvider>
        );

        //Assert
        await waitFor(() => {
            expect(screen.getByText("Error!")).toBeInTheDocument();
        });
    });

    it('throws an error when fetching game fails',async () => {
        //Arrange
        const gameId = compactGame1.id;
        const queryClient = new QueryClient({
            defaultOptions: { queries: { retry: false } }
        });

        const mockedAxios = axios as unknown as Mocked<AxiosStatic>;
        mockedAxios.get.mockResolvedValueOnce(new Error("Network error"));

        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter initialEntries={[`/games/${gameId}`]}>
                    <Routes>
                        <Route path="/games/:gameId" element={
                            <ErrorBoundary fallback={<div>Error!</div>}>
                                <GamePage/>
                            </ErrorBoundary>
                        }/>
                    </Routes>
                </MemoryRouter>
            </QueryClientProvider>
        );

        //Assert
        await waitFor(() => {
            expect(screen.getByText("Error!")).toBeInTheDocument();
        });
    })

})