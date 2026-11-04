import {Typography, Container, Stack, Button, useTheme} from "@mui/material";
import VideogameAssetOffIcon from "@mui/icons-material/VideogameAssetOff";
import SearchIcon from "@mui/icons-material/Search";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import ArrowUpwardIcon from "@mui/icons-material/ArrowUpward";
import {useState} from "react";
import {useGamesList} from "../hooks/api/games/useGamesList.tsx";
import {useFavoriteGames} from "../hooks/api/favoriteGames/useFavoriteGames.tsx";
import {useOwnedGames} from "../hooks/api/games/useOwnedGames.tsx";
import {SearchIconWrapper, StyledInputBase, Search} from "../components/controls/Search.tsx";
import {GameCardList} from "../components/lists/GameCardList.tsx";
import {useNavigate} from "react-router-dom";
import {GameModeSelectionDialog} from "../components/dialogs/GameModeSelectionDialog.tsx";

export function GameLibraryPage() {
    const {games} = useGamesList();
    const {ownedGames} = useOwnedGames();
    const {favorites} = useFavoriteGames();
    const [sortAbc, setSortAbc] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");
    const [isSelectingGameMode, setIsSelectingGameMode] = useState(false);
    const theme = useTheme();
    const navigate = useNavigate();

    const ownedIds = new Set(ownedGames?.map(o => o.gameId) ?? []);

    const sortedGames = (games ?? [])
        .filter(g => ownedIds.has(g.id))
        .filter(g =>
            !searchTerm ||
            g.name.toLowerCase().includes(searchTerm.toLowerCase())
        )
        .sort((g1, g2) =>
            sortAbc
                ? g1.name.localeCompare(g2.name)
                : g2.name.localeCompare(g1.name)
        );

    return (
        <>
            <Stack
                direction={{lg: "row", sx: "column"}}
                alignItems={"center"}
                justifyContent={"space-between"}
            >
                <Typography variant={"h2"}>Games</Typography>
                <Stack direction={"row"}>
                    <Button onClick={() => setSortAbc(prev => !prev)}>
                        <Stack direction={"row"}>
                            <Typography color={theme.palette.text.primary}>abc</Typography>
                            {sortAbc ? (
                                <ArrowDownwardIcon sx={{color: theme.palette.text.primary}}/>
                            ) : (
                                <ArrowUpwardIcon sx={{color: theme.palette.text.primary}}/>
                            )}
                        </Stack>
                    </Button>
                    <Search>
                        <SearchIconWrapper>
                            <SearchIcon/>
                        </SearchIconWrapper>
                        <StyledInputBase
                            placeholder="Search…"
                            inputProps={{"aria-label": "search"}}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </Search>
                </Stack>
            </Stack>

            {sortedGames.length !== 0 ? (
                <GameCardList games={sortedGames}
                              favorites={favorites}
                              onSelectGame={() => setIsSelectingGameMode(true)}
                />
            ) : (
                <Container>
                    <Stack alignItems="center"
                           justifyContent="center"
                           spacing={2}>
                        <VideogameAssetOffIcon/>
                        <Stack direction={"column"}>
                            <Typography variant={"h5"}>
                                Het lijkt erop dat je nog geen games hebt...
                            </Typography>
                            <Typography>Je kan nieuwe games kopen op de
                                <Button
                                    color={"secondary"}
                                    onClick={() => navigate("/store")}
                                >
                                    Winkel pagina
                                </Button>
                            </Typography>
                        </Stack>
                    </Stack>
                </Container>
            )}
            <GameModeSelectionDialog
                isOpen={isSelectingGameMode}
                onClose={() => setIsSelectingGameMode(false)}
            />

        </>
    );
}