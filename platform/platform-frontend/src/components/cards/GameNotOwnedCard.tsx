import {Button, Card, CardContent, Stack, Typography, useTheme} from "@mui/material";
import {Link} from "react-router-dom";


export function GameNotOwnedCard(){
    const theme = useTheme();
    return(
        <>
            <>
                <Card
                    sx={{
                        mt:2,
                        aspectRatio:"16/9",
                        maxHeight:"70%"
                }}
                >
                    <CardContent
                        sx={{
                            flexGrow: 1,
                            p: 4,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            height:"100%",
                            color:theme.palette.primary.main
                        }}
                    >
                        <Stack direction={"column"}
                               alignItems={"center"}
                               justifyContent={"center"}
                               sx={{textAlign:"center", height:"100%"}}
                        >
                            <Typography variant={"h4"} sx={{mt:2}}>
                                Je bezit deze game niet...
                            </Typography>
                            <Typography variant={"h6"} sx={{mt:2}}>
                                Bezoek de winkel om deze game aan te kopen
                            </Typography>
                            <Button
                                sx={{mt:2}}
                                variant={"contained"}
                                color={"primary"}
                                component={Link}
                                to={"/store"}
                            >Bezoek winkel</Button>
                        </Stack>
                    </CardContent>
                </Card>
            </>
        </>
    )
}