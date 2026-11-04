import {Button, Stack, Typography} from "@mui/material";
import {useState} from "react";
import {SizeSelector} from "../components/SizeSelector.tsx";
import {useNavigate} from "react-router-dom";
import {useGetOngoingGameBoard} from "../hooks/useGetOngoingGameBoard.ts";

export function WelcomePage() {
    const {gameState, isGamePending} = useGetOngoingGameBoard();
    const navigate = useNavigate();
    const [isSelectingSize, setIsSelectingSize] = useState(false);
    const sizes = [9, 13, 19];

    if (isGamePending) {
        return <div>Loading...</div>
    }
    function handleNavigate(){
        navigate(`/game/${gameState?.id}`)
    }
    console.log(gameState);

    if(gameState != null && !gameState.isAiGame){
        handleNavigate();
    }

    return (
        <>
            {!isSelectingSize &&
                <Stack justifyContent={"center"}
                       alignItems={"center"}
                       sx={{height: "100svh"}}>
                    <Typography variant={"h2"}>
                        Welcome to Go!
                    </Typography>
                    <Button
                        variant={"contained"}
                        sx={{mt: 2}}
                        onClick={() => setIsSelectingSize(true)}
                    >
                        Nieuw spel starten
                    </Button>
                    {gameState != null &&
                        <>
                            <Typography variant={"h6"} sx={{mt:2}}>Je hebt nog een spel tegen een AI</Typography>
                            <Button
                                sx={{mt:1}}
                                variant={"contained"}
                                color={"secondary"}
                                onClick={handleNavigate}
                            >
                                Verder spelen
                            </Button>
                        </>
                    }
                </Stack>
            }

            {isSelectingSize &&
                <SizeSelector
                    onBack={() => setIsSelectingSize(false)}
                    sizes={sizes}
                />
            }
        </>
    )
}