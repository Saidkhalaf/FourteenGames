import {Typography, Container, Stack, Button, useTheme, Card, CardContent} from "@mui/material";
import {useGamesList} from "../hooks/api/games/useGamesList.tsx";
import {useOwnedGames} from "../hooks/api/games/useOwnedGames.tsx";
import VideogameAssetOffIcon from '@mui/icons-material/VideogameAssetOff';
import SearchIcon from '@mui/icons-material/Search';
import {SearchIconWrapper, StyledInputBase, Search} from "../components/controls/Search.tsx";
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import {useState} from "react";
import {StoreGameCardList} from "../components/lists/StoreGameCardList.tsx";
import {usePlatformUser} from "../hooks/usePlatformUser.tsx";
import {creditName} from "../config/theme/names.ts";
import {AddCreditsDialog} from "../components/dialogs/AddCreditsDialog.tsx";

export function GameStorePage() {
    const {games} = useGamesList();
    const {ownedGames} = useOwnedGames();
    const [isAddingCredits, setIsAddingCredits] = useState(false);
    const [sortAbc, setSortAbc] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");
    const theme = useTheme();
    const {platformUser} = usePlatformUser();
    const ownedIds = new Set(ownedGames?.map(o => o.gameId) ?? []);

    const filteredGames = (games ?? [])
        .filter(g => !ownedIds.has(g.id))
        .filter(g => !searchTerm || g.name.toLowerCase().includes(searchTerm.toLowerCase()))
        .sort((g1, g2) => sortAbc ? g1.name.localeCompare(g2.name) : g2.name.localeCompare(g1.name));

    return (
        <>
            <Stack direction={{lg: "row", sx: "column"}} alignItems="center" justifyContent="space-between">
                <Typography variant="h2">Store</Typography>
                <Stack direction="row">
                    <Button onClick={() => setSortAbc(prev => !prev)}>
                        <Stack direction="row">
                            <Typography color={theme.palette.text.primary}>abc</Typography>
                            {sortAbc
                                ? <ArrowDownwardIcon sx={{color: theme.palette.text.primary}}/>
                                : <ArrowUpwardIcon sx={{color: theme.palette.text.primary}}/>
                            }
                        </Stack>
                    </Button>
                    <Search>
                        <SearchIconWrapper>
                            <SearchIcon/>
                        </SearchIconWrapper>
                        <StyledInputBase
                            placeholder="Search…"
                            inputProps={{'aria-label': 'search'}}
                            value={searchTerm}
                            onChange={e => setSearchTerm(e.target.value)}
                        />
                    </Search>
                </Stack>
            </Stack>

            <Stack direction="row">
                <Card sx={{borderRadius: 4}}>
                    <CardContent>
                        <Typography
                            variant="h6"
                            color={theme.palette.primary.main}
                            data-testid={"SaldoVisual"}
                        >
                            Saldo: {platformUser.credits.toFixed(2)} {creditName}
                        </Typography>
                        <Button sx={{mt: 1}} variant="contained" onClick={() => setIsAddingCredits(true)}>
                            Saldo toevoegen
                        </Button>
                    </CardContent>
                </Card>
            </Stack>

            {filteredGames.length !== 0
                ? <StoreGameCardList games={filteredGames} favorites={[]}/>
                : (
                    <Container>
                        <Stack alignItems="center" justifyContent="center" spacing={2}>
                            <VideogameAssetOffIcon/>
                            <Typography variant="h5" color="info">Geen games gevonden</Typography>
                        </Stack>
                    </Container>
                )
            }

            <AddCreditsDialog
                onClose={() => setIsAddingCredits(false)}
                isOpen={isAddingCredits}
                presetValues={[10, 20, 50, 100]}
            />
        </>
    );
}
