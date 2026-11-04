import {Box, Stack, Card, Typography, Divider, FormControlLabel, Switch, IconButton,useTheme} from "@mui/material";
import {AchievementPreviewList} from "./lists/AchievementPreviewList.tsx";
import {useState} from "react";
import type {Achievement} from "../models/achievement/Achievement.ts";
import {usePlatformUser} from "../hooks/usePlatformUser.tsx";
import {UnlockedAchievementCount} from "./UnlockedAchievementCount.tsx";
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';

interface GameFullAchievementListProps {
    gameName: string;
    achievements: Achievement[];
    openedByDefault?: boolean;
}

export function GameFullAchievementList({gameName, achievements, openedByDefault = false}: GameFullAchievementListProps) {
    const [isExpanded, setIsExpanded] = useState(openedByDefault);
    const {platformUser} = usePlatformUser();
    const [filter, setFilter] = useState<"all" | "achieved" | "not-achieved">("all");
    const theme = useTheme();

    const unlockedCount = achievements.filter(a =>
        platformUser.achievements.some(u => u.achievementId === a.id)
    ).length;

    return (
        <Stack direction={"column"} sx={{mt: 2}}>
            <Typography variant={"h4"}>{gameName}</Typography>
            {unlockedCount != achievements.length &&
                <Card sx={{minHeight: 150, display: "flex"}}>
                    <Box sx={{
                        flex: 1,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        position: "relative"
                    }}>
                        <UnlockedAchievementCount unlockedCount={unlockedCount} totalCount={achievements.length}/>
                        <IconButton
                            size={"large"}
                            color={"primary"}
                            sx={{
                                position: "absolute",
                                right: 2
                            }}
                            onClick={() => setIsExpanded(!isExpanded)}
                        >
                            {isExpanded ?
                                <ChevronRightIcon/>
                                :
                                <ChevronLeftIcon/>
                            }
                        </IconButton>
                    </Box>
                    {isExpanded && unlockedCount != achievements.length &&
                        <>
                            <Divider orientation="vertical" flexItem/>
                            <Box sx={{flex: 3, p: 2}}>
                                <FormControlLabel
                                    control={
                                        <Switch
                                            checked={filter === "not-achieved"}
                                            onChange={(e) =>
                                                setFilter(e.target.checked ? "not-achieved" : "all")
                                            }
                                            size="small"
                                        />
                                    }
                                    sx={{color:theme.palette.primary.main}}
                                    label={"Alleen niet behaalde weergeven"}
                                />
                                <AchievementPreviewList
                                    achievements={achievements}
                                    filter={filter}
                                    amountToShow={achievements.length}/>
                            </Box>
                        </>
                    }
                </Card>
            }

            {unlockedCount === achievements.length &&
                <Card sx={{minHeight: 150, display: "flex", p:2}}>
                    <Box sx={{
                        flex: 1,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        position: "relative"
                    }}>
                        <UnlockedAchievementCount unlockedCount={unlockedCount} totalCount={achievements.length}/>
                    </Box>
                    <Box sx={{flex: 3, p: 2}} display={"flex"} flexDirection={"column"} justifyContent={"center"}>
                        <Typography variant={"h4"} color={"primary"}>
                            Proficiat!
                        </Typography>
                        <Typography variant={"h5"} color={"primary"}>
                            Je hebt alle achievements voor
                            <Typography component={"span"} variant={"h5"} color={"secondary"} sx={{fontWeight:"bold", mx:1}}>
                                {gameName}
                            </Typography>
                            vrijgespeeld 🎉
                        </Typography>
                    </Box>
                </Card>
            }

        </Stack>
    )
}