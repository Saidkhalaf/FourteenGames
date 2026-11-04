import {Typography, useMediaQuery} from "@mui/material";
import {useParams} from "react-router-dom";
import {GamePlayer} from "../components/GamePlayer.tsx";
import {useGame} from "../hooks/api/games/useGame.tsx";
import {RotateDeviceInstruction} from "../components/RotateDeviceInstruction.tsx";
import {ErrorCard} from "../components/cards/ErrorCard.tsx";
import {useGameIsFavorite} from "../hooks/api/favoriteGames/useIsFavoriteGame.tsx";
import {AchievementPreviewList} from "../components/lists/AchievementPreviewList.tsx";
import {usePlatformUser} from "../hooks/usePlatformUser.tsx";
import {GameNotOwnedCard} from "../components/cards/GameNotOwnedCard.tsx";
import {useLobbyByGameSessionId} from "../hooks/api/lobby/useLobbyByGameSessionId.tsx";
import {CurrentPlayersOverlay} from "../components/overlay/CurrentPlayersOverlay.tsx";

export function GamePage() {
    const {platformUser} = usePlatformUser();
    const {gameId, gameSessionId} = useParams();
    const {game} = useGame(gameId!);
    const {isFavorite} = useGameIsFavorite(gameId!);
    const isPortrait = useMediaQuery('(orientation: portrait)');
    const {lobby} = useLobbyByGameSessionId(gameSessionId!, gameSessionId != null);
    if (!game || !gameId) {
        throw new Error("Something went wrong with fetching a game...")
    }

    if(!platformUser.ownedCopies.map(copy => copy.gameId).includes(gameId)){
        return <GameNotOwnedCard/>
    }

    return (
        <>
            <Typography variant={"h2"}>
                {game.name}
            </Typography>
            {isPortrait ?
                <RotateDeviceInstruction/>
                :
                <>
                    {game.url ?
                        <>
                            {lobby &&
                                <CurrentPlayersOverlay players={lobby?.players}/>
                            }
                            <GamePlayer gameId={game.id}
                                        gameUrl={game.url}
                                        isFavorite={isFavorite}/>
                        </>
                        :
                        <ErrorCard
                            title={"Ohnee..."}
                            description={"Er ging iets mis met het ophalen van het spel, probeer later opnieuw."}
                        />
                    }
                </>
            }
            <Typography variant={"h3"}
                        sx={{mt: 4}}>Achievements</Typography>
            <AchievementPreviewList filter={"not-achieved"}
                                    achievements={game.achievements}/>
        </>
    )
}