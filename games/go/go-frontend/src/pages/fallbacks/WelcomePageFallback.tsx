import {Card, Skeleton} from "@mui/material";


export function WelcomePageFallback() {
    return (
        <Card
            sx={{
                width: "100%",
                height: "300px",
                borderRadius: "12px",
                overflow: "hidden"
            }}
        >
            <Skeleton variant="rectangular" width="100%" height="100%"/>
        </Card>
    )
}