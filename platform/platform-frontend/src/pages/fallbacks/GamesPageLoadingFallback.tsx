import {Card, Skeleton, Stack} from "@mui/material";


export function GamesPageLoadingFallback() {
    return (
        <>
            <Skeleton variant="text"
                      sx={{fontSize: '2rem', width: "50%"}}/>
            <Stack flexDirection={"row"}
                   flexWrap="wrap"
                   height={"75%"}>
                
                {[...Array(4)].map((_, i) => (
                    <Card
                        key={i}
                        sx={{
                            width: { lg: "20%", xs: "40%" },
                            height: "60%",
                            marginRight: "5%",
                            marginBottom: "5%",
                            cursor: "pointer",
                        }}
                    >
                        <Skeleton variant="rectangular" width="100%" height="100%" />
                    </Card>
                ))}

            </Stack>
        </>
    )
}