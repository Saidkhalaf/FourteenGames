import {Box, Card, Collapse, IconButton, Typography, useTheme} from "@mui/material";
import {usePlatformUsers} from "../../hooks/api/user/usePlatformUsers.tsx";
import type {Player} from "../../models/lobby/Player.ts";
import CurrentPlayerCard from "../cards/CurrentPlayerCard.tsx";
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {useState} from "react";

interface CurrentPlayersOverlayProps{
    players:Player[],
}
export function CurrentPlayersOverlay({players}: CurrentPlayersOverlayProps) {
    const [isOpen, setIsOpen] = useState(true);
    const {userData, isError} = usePlatformUsers(players.map(u => u.userId))
    const theme = useTheme();

    if(isError || !userData){
        return (
            <Card
                sx={{
                    position: "absolute",
                    top:100,
                    right:30,
                    zIndex: "999",
                    p: 2
                }}
            >
                <Typography variant={"h6"} color={"primary"}>
                    Er ging iets mis met het ophalen van de spelers...
                </Typography>
            </Card>
        )
    }

    return (
        <Card
            sx={{
                top:100,
                right:30,
                position: "absolute",
                zIndex: "999",
                minWidth: 250,
            }}
        >
            <Box
                sx={{
                    p: 2,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",

                    pb: isOpen ? 1 : 2
                }}
            >
                <Typography variant={"h6"} color={"primary"}>
                   Spelers in dit spel:
                </Typography>
                <IconButton
                    onClick={() => setIsOpen(!isOpen)}
                    size="small"
                    aria-label="toggle player list"
                    sx={{color: theme.palette.primary.main}}
                >
                    {isOpen ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                </IconButton>
            </Box>

            <Collapse in={isOpen} timeout="auto" unmountOnExit>
                <Box sx={{ p: 2, pt: 0 }}>
                    {userData!.map(u => (
                        <CurrentPlayerCard
                            key={"user-" + u.id}
                            userName={u.userName}
                        />
                    ))}
                </Box>
            </Collapse>
        </Card>
    );
}