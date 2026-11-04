import {Box, CircularProgress, useTheme} from "@mui/material";
import MilitaryTechIcon from "@mui/icons-material/MilitaryTech";
import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";

interface AchievementCircularPercentageProps {
    unlockedCount: number;
    totalCount: number;
}

export function AchievementCircularPercentage({
                                                  unlockedCount,
                                                  totalCount,
                                              }: AchievementCircularPercentageProps) {
    const theme = useTheme();
    const percentage =
        totalCount > 0 ? (unlockedCount / totalCount) * 100 : 0;
    const size = 100;

    return (
        <Box
            sx={{
                position: "relative",
                width: size,
                height: size,
            }}
        >
            <CircularProgress
                variant="determinate"
                value={100}
                size={size}
                thickness={4}
                sx={{
                    color: theme.palette.grey[100],
                    position: "absolute",
                    top: 0,
                    left: 0,
                }}
            />
            <CircularProgress
                variant="determinate"
                value={percentage}
                size={size}
                thickness={4}
                sx={{
                    color: theme.palette.secondary.main,
                    position: "absolute",
                    top: 0,
                    left: 0,
                }}
            />
            <Box
                sx={{
                    position: "absolute",
                    inset: 0,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                }}
            >
                {unlockedCount != totalCount ?
                    <MilitaryTechIcon
                        fontSize="large"
                        sx={{color: theme.palette.secondary.main}}
                    />
                    :
                    <EmojiEventsIcon fontSize={"large"} color={"secondary"}/>
                }
            </Box>
        </Box>
    );
}