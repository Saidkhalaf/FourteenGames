import {Typography, Container, Stack, Button, useTheme} from "@mui/material";
import {useGamesList} from "../hooks/api/games/useGamesList.tsx";
import VideogameAssetOffIcon from '@mui/icons-material/VideogameAssetOff';
import SearchIcon from '@mui/icons-material/Search';
import {SearchIconWrapper, StyledInputBase, Search} from "../components/controls/Search.tsx";
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import {useState} from "react";
import {StoreGameCardSignedOut} from "../components/cards/StoreGameCardSignedOut.tsx";

export function GameStorePageNotSignedIn() {
    const {games} = useGamesList();
    const [sortAbc, setSortAbc] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");
    const theme = useTheme();


    const filteredGames = (games ?? [])
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

            {filteredGames.length !== 0
                ? <Stack direction={"row"} flexWrap="wrap" height={"75%"} sx={{pt:2}}>
                    {games.map(game => <StoreGameCardSignedOut game={game} key={game.id}/>)}
                </Stack>
                : (
                    <Container>
                        <Stack alignItems="center" justifyContent="center" spacing={2}>
                            <VideogameAssetOffIcon/>
                            <Typography variant="h5" color="info">Geen games gevonden</Typography>
                        </Stack>
                    </Container>
                )
            }
        </>
    );
}
