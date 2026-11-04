import {describe, expect, it, type Mocked, vi} from "vitest";
import {render, screen,} from "@testing-library/react";
import {FallbackWrapper} from "../../src/components/FallbackWrapper.tsx";
import {GameCard} from "../../src/components/cards/GameCard.tsx";
import {compactGame1} from "../data/TestGames.ts";
import axios, {type AxiosStatic} from "axios";
import {MemoryRouter, Route, Routes} from "react-router-dom";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

vi.mock('axios')

describe('GameCard', () => {
    it('renders correctly with full provided compactgame', () => {
        //Arrange
        const queryClient = new QueryClient();
        const mockedAxios = axios as unknown as Mocked<AxiosStatic>;
        mockedAxios.post.mockResolvedValueOnce({data: compactGame1})
        mockedAxios.delete.mockResolvedValueOnce({})

        //Act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter initialEntries={[`/`]}>
                    <Routes>
                        <Route path="/"
                               element={
                                   <FallbackWrapper
                                       loadingFallback={<div>Loading</div>}
                                       errorFallback={<div>Error</div>}
                                   >
                                       <GameCard game={compactGame1} isFavorite={false} onSelectGame={() => {console.log("selected game")}}/>
                                   </FallbackWrapper>
                               }
                        />
                    </Routes>
                </MemoryRouter>
            </QueryClientProvider>
        );

        //Assert
        const nameElement = screen.getByText(compactGame1.name);
        expect(nameElement).toBeInTheDocument();
        const image = screen.getByRole("img");
        expect(image).toBeInTheDocument();
        expect(image).toHaveAttribute("src", compactGame1.icon);
    });

});