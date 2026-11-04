import {Card, CardContent, CardMedia, Chip, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {GameBoardPreview} from "../gameboard/GameBoardPreview.tsx";
import {useStartNewAiGame} from "../../hooks/useStartNewAiGame.ts";

interface SizeSelectionCardProps{
    size:number;
}

export function SizeSelectionCard({size}:SizeSelectionCardProps){
    const {createGame} = useStartNewAiGame();
    const navigate = useNavigate();

    async function handleGameCreation(){
        const data = await createGame({size:size});
        navigate(`/game/${data.id}`);
    }

    return(
        <Card
            sx={{
                height: 300,
                width:300,
                display: "flex",
                flexDirection: "column",
                mt: 4,
                m:2,
                cursor:"pointer"
            }}
            onClick={handleGameCreation}
        >
            <CardMedia
                sx={{height: "80%", background:"white", color:"black", position:"relative"}}
            >
                <Chip label="Difficulty" color={"secondary"}
                      sx={{
                            position: "absolute",
                            top: 8,
                            right: 8,
                            zIndex: 1,
                        }}
                />
                <GameBoardPreview size={size}/>
            </CardMedia>
            <CardContent sx={{
                height: "20%",
                display:"flex",
                justifyContent:"center"
            }}>
                <Typography variant={"h6"}>
                    {size}x{size}
                </Typography>
            </CardContent>
        </Card>
    )
}