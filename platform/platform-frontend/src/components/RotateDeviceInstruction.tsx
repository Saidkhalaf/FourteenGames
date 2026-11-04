import {Card, CardContent, Stack, Typography, useTheme} from "@mui/material";
import ScreenRotationIcon from '@mui/icons-material/ScreenRotation';

export function RotateDeviceInstruction(){
    const theme = useTheme();
    return(
        <>
            <>
                <Card
                    sx={{mt:2}}
                >
                    <CardContent
                        sx={{
                            flexGrow: 1,
                            p: 4,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            color:theme.palette.primary.main
                        }}
                    >
                        <Stack direction={"column"}
                               alignItems={"center"}
                            sx={{textAlign:"center"}}
                        >
                            <ScreenRotationIcon sx={{fontSize:100}}/>
                            <Typography variant={"h4"} sx={{mt:2}}>
                                Draai je apparaat voor een goede spel ervaring!
                            </Typography>
                        </Stack>
                    </CardContent>
                </Card>
            </>
        </>
    )
}