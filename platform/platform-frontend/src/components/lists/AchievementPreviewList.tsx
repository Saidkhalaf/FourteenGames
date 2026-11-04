import {Stack} from "@mui/material";
import type {Achievement} from "../../models/achievement/Achievement.ts";
import {AchievementPreviewCard} from "../cards/AchievementPreviewCard.tsx";
import {usePlatformUser} from "../../hooks/usePlatformUser.tsx";

interface AchievementPreviewListProps {
    achievements: Achievement[];
    filter: "all"|"achieved"|"not-achieved"
    amountToShow?: number;
}

export function AchievementPreviewList({achievements, filter, amountToShow = 3}: AchievementPreviewListProps) {
    const {platformUser} = usePlatformUser();

    const isUnlocked = (achievementId: string) =>
        platformUser.achievements.some(u => u.achievementId === achievementId);

    const filtered = achievements.filter(a => {
        if (filter === "all") return true;
        if (filter === "achieved") return isUnlocked(a.id);
        if (filter === "not-achieved") return !isUnlocked(a.id);
        return true;
    });

    return (
        <Stack
            direction="row"
            flexWrap="wrap"
            sx={{ width: "100%", minHeight: 20, mt: 2 }}
            gap={2}
        >
            {filtered.slice(0, amountToShow).map((achievement) =>
                <AchievementPreviewCard
                    data-testid="achievement-card"
                    key={"achievement" + achievement.id}
                    name={achievement.name}
                    description={achievement.description}
                    unlocked={isUnlocked(achievement.id)}
                />
            )}
        </Stack>
    );
}