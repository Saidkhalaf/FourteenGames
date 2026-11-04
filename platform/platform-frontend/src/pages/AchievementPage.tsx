import {Stack, Typography} from "@mui/material";
import {GameFullAchievementList} from "../components/GameFullAchievementList.tsx";
import {useGamesList} from "../hooks/api/games/useGamesList.tsx";
import {useOwnedGames} from "../hooks/api/games/useOwnedGames.tsx";

export function AchievementPage() {
    const { games } = useGamesList();
    const { ownedGames } = useOwnedGames();
    const ownedIds = new Set(ownedGames?.map(o => o.gameId) ?? []);

    return (
        <Stack direction={{ sx: "column" }}>
            <Stack direction={"column"}>
                <Typography variant={"h2"}>Achievements</Typography>
            </Stack>

            {games
                .filter(game => ownedIds.has(game.id))
                .filter(game => game.achievements.length !== 0)
                .map(game => (
                    <GameFullAchievementList
                        key={"game" + game.id}
                        openedByDefault={true}
                        gameName={game.name}
                        achievements={game.achievements}
                    />
                ))
            }
        </Stack>
    );
}
