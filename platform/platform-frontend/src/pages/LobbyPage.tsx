import {Card, InputLabel, MenuItem, Select, Stack, Typography} from "@mui/material";
import {useGamesList} from "../hooks/api/games/useGamesList.tsx";
import {usePlatformUser} from "../hooks/usePlatformUser.tsx";
import {useSelectionStore} from "../stores/selectionStore.ts";
import {useLobbyList} from "../hooks/api/lobby/useLobbyList.tsx";
import {LobbyTable} from "../components/tables/LobbyTable.tsx";

export function LobbyPage() {
    const {platformUser} = usePlatformUser();
    const {lobbies} = useLobbyList();
    const {games} = useGamesList();
    const selectedGameId = useSelectionStore((state) => state.selectedGameId);
    const setSelectedGameId = useSelectionStore((state) => state.setSelectedGameId);

    const ownedCopyIds = platformUser.ownedCopies.map(copy => copy.gameId);
    const ownedGames = games.filter(game => ownedCopyIds.includes(game.id));
    const filteredGames = selectedGameId
        ? games.filter(g => g.id === selectedGameId)
        : games;

    return (
        <>
            <Typography variant={"h2"}>
                Lobbies
            </Typography>
            <Stack direction="row"
                   sx={{height: "90%"}}
                   gap={2}>
                <Card
                    sx={{
                        flex: 1,
                        height: "100%",
                        p: 2
                    }}
                >
                    <Typography variant={"h4"}
                                color={"primary"}>Opties</Typography>
                    <InputLabel id="game-label"
                                sx={{mt: 2, mb: 1}}>Game</InputLabel>
                    <Select
                        labelId="game-label"
                        value={selectedGameId ?? "all"}
                        onChange={(e) =>
                            setSelectedGameId(e.target.value === "all" ? null : e.target.value)
                        }
                    >
                        <MenuItem value="all">Alles</MenuItem>
                        {ownedGames.map(game => (
                            <MenuItem key={"game" + game.id} value={game.id}>
                                {game.name}
                            </MenuItem>
                        ))}
                    </Select>
                </Card>
                <LobbyTable
                    lobbies={lobbies}
                    games={filteredGames}
                />
            </Stack>
        </>
    )
}