import {Card, CardContent, CardMedia, Stack, Typography} from "@mui/material";
import MilitaryTechIcon from "@mui/icons-material/MilitaryTech";
import {theme} from "../../config/theme/theme.ts";
import LockIcon from '@mui/icons-material/Lock';

interface AchievementPreviewCardProps {
    name: string;
    description: string;
    unlocked: boolean;
}

export function AchievementPreviewCard({name, description, unlocked}: AchievementPreviewCardProps) {
    return (
        <Card sx={{minWidth: 350}}>
            <Stack direction={"row"}
                   sx={{
                       height: "100%"
                   }}
            >
                <CardMedia
                    sx={{
                        flex: 1,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        color:unlocked? theme.palette.primary.contrastText : theme.palette.secondary.main,
                        background:unlocked? theme.palette.secondary.main : theme.palette.primary.contrastText
                    }}
                >
                    {unlocked ?
                        <MilitaryTechIcon fontSize={"large"}/>
                        :
                        <LockIcon fontSize={"large"}/>
                    }
                </CardMedia>

                <CardContent sx={{flex: 3, color: theme.palette.primary.main}}>
                    <Typography variant="subtitle1"
                                fontWeight={600}>
                        {name}
                    </Typography>
                    <Typography variant="body2">
                        {description}
                    </Typography>
                </CardContent>
            </Stack>
        </Card>
    )
}