import {Button, Card, CardContent, Stack, Typography, useTheme} from "@mui/material";
import {useNavigate} from "react-router-dom";

interface ErrorCardProps {
    title: string;
    description: string;
    renderBackToHomeButton?: boolean;
}

export function ErrorCard({title, description, renderBackToHomeButton = false}: ErrorCardProps) {
    const theme = useTheme();
    const navigate = useNavigate();
    return (
        <Card
            sx={{
                mt: 2,
                aspectRatio: "16/9",
                maxHeight: "70%"
            }}
        >
            <CardContent
                sx={{
                    flexGrow: 1,
                    p: 4,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    height: "100%",
                    color: theme.palette.primary.main
                }}
            >
                <Stack direction={"column"}
                       alignItems={"center"}
                       justifyContent={"center"}
                       sx={{textAlign: "center", height: "100%"}}
                >
                    <Typography variant={"h2"}
                                sx={{mt: 2}}>
                        {title}
                    </Typography>
                    <Typography variant={"h4"}
                                sx={{mt: 2}}>
                        {description}
                    </Typography>

                    {renderBackToHomeButton &&
                        <Button
                            variant={"contained"}
                            sx={{mt: 2}}
                            onClick={() => {
                                navigate("/")
                            }}
                        >
                            Naar het startmenu
                        </Button>
                    }
                </Stack>
            </CardContent>
        </Card>
    )
}