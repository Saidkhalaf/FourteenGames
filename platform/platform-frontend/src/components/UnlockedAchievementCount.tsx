import {Typography, Stack} from "@mui/material";
import {AchievementCircularPercentage} from "./AchievementCircularPercentage.tsx";

interface UnlockedAchievementCountProps {
    unlockedCount: number;
    totalCount: number;
}

export function UnlockedAchievementCount({unlockedCount, totalCount}: UnlockedAchievementCountProps) {
    return (
        <Stack
            direction={"column"}
            sx={{maxWidth:150}}
            alignItems={"center"}
        >
            <AchievementCircularPercentage unlockedCount={unlockedCount} totalCount={totalCount}/>
            <Typography component={"output"} variant={"h6"} sx={{mt:2}} color={"secondary"}>{(unlockedCount/totalCount) * 100}% behaald</Typography>
        </Stack>
    )
}