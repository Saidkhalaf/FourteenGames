import {Card, CardContent, Stack, Typography, useTheme} from "@mui/material";

interface ErrorCardProps{
    title:string;
    description:string;
}

export function ErrorCard({title, description}:ErrorCardProps){
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
                            <Typography variant={"h2"} sx={{mt:2}}>
                                {title}
                            </Typography>
                            <Typography variant={"h4"} sx={{mt:2}}>
                                {description}
                            </Typography>
                        </Stack>
                    </CardContent>
                </Card>
            </>
        </>
    )
}