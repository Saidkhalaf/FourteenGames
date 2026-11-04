import {Navigate, Route, Routes} from "react-router-dom";
import {ErrorCard} from "../cards/ErrorCard.tsx";
import {GamesPageLoadingFallback} from "../../pages/fallbacks/GamesPageLoadingFallback.tsx";
import {GameLibraryPage} from "../../pages/GameLibraryPage.tsx";
import {GamePageLoadingFallback} from "../../pages/fallbacks/GamePageLoadingFallback.tsx";
import {GamePage} from "../../pages/GamePage.tsx";
import {Typography} from "@mui/material";
import {ProfilePage} from "../../pages/ProfilePage.tsx";
import {UserPreferencePage} from "../../pages/UserPreferencePage.tsx";
import {FriendListPage} from "../../pages/FriendListPage.tsx";
import {AchievementPage} from "../../pages/AchievementPage.tsx";
import {createFallbackWrapper, createFallbackWrapperWithRouteGuard} from "../factories/fallbackWrapperFactory.tsx";
import {GameStorePage} from "../../pages/GameStorePage.tsx";
import {LobbyPage} from "../../pages/LobbyPage.tsx";
import {LobbyManagementPage} from "../../pages/LobbyManagementPage.tsx";
import {GameStorePageNotSignedIn} from "../../pages/GameStorePageNotSignedIn.tsx";
import {useSecurityStore} from "../../stores/securityStore.ts";

export function ReactRouterConfig() {
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    return (
        <Routes>
            <Route path="/"
                   element={<Navigate to="/store"
                                      replace/>}/>
            <Route path={"/library"}
                   element={
                       createFallbackWrapperWithRouteGuard({
                           children: <GameLibraryPage/>,
                           errorFallback: <ErrorCard
                               title={"Ohnee..."}
                               description={"Er ging iets mis met het ophalen van games..."}
                           />,
                           loadingFallback: <GamesPageLoadingFallback/>
                       })
                   }/>

            <Route path={"/store"}
                element={
                    createFallbackWrapper({
                        children: loggedInUser ? <GameStorePage/>:<GameStorePageNotSignedIn/>,
                        errorFallback: <ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het ophalen van games..."}
                        />,
                        loadingFallback: <GamesPageLoadingFallback/>
                    })
                }
            />

            <Route
                path={"/games/:gameId/:gameSessionId?"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children: <GamePage/>,
                        errorFallback: <ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <GamePageLoadingFallback/>
                    })
                }/>

            <Route
                path={"/profile/:userName"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children: <ProfilePage/>,
                        errorFallback: <ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <Typography>FALLBACK</Typography>
                    })
                }/>

            <Route
                path={"/userPreferences"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children: <UserPreferencePage/>,
                        errorFallback: <ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <Typography>FALLBACK</Typography>
                    })
                }/>

            <Route
                path={"/friends"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children: <FriendListPage/>,
                        errorFallback: <ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <Typography>FALLBACK</Typography>
                    })
                }/>

            <Route
                path={"/achievements"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children:<AchievementPage/>,
                        errorFallback:<ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <Typography>FALLBACK</Typography>
                    })
                }/>

            <Route
                path={"/Lobbies"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children:<LobbyPage/>,
                        errorFallback:<ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <Typography>FALLBACK</Typography>
                    })
                }/>

            <Route
                path={"/lobby/:lobbyId"}
                element={
                    createFallbackWrapperWithRouteGuard({
                        children:<LobbyManagementPage/>,
                        errorFallback:<ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het laden van deze pagina..."}
                        />,
                        loadingFallback: <Typography>FALLBACK</Typography>
                    })
                }
            />

        </Routes>
    )
}