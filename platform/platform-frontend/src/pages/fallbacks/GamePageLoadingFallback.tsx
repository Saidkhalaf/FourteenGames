import {Card, Skeleton} from "@mui/material";


export function GamePageLoadingFallback() {
    return (
        <>
            <Skeleton variant="text"
                      sx={{fontSize: '2rem', width: "50%"}}/>
            <Card
                sx={{
                    mt: 2,
                    maxHeight: "75svh",
                    aspectRatio: "16/9",
                    display: "flex",
                    flexDirection: "column",
                    overflow: "hidden",
                    position: "relative",
                }}
            >
                <Skeleton variant="rectangular"
                          width={"100%"}
                          height={"100%"}/>
            </Card>
        </>
    )
}