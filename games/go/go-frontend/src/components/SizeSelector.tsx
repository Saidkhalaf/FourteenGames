import {Button, Stack, Typography} from "@mui/material";
import {SizeSelectionCard} from "./cards/SizeSelectionCard.tsx";

interface SizeSelectorProps {
    sizes: number[];
    onBack: () => void;
}

export function SizeSelector({sizes, onBack}: SizeSelectorProps) {
    return (
        <Stack
            sx={{p: 4}}
        >
            <Stack direction={"row"}>
                <Button
                    variant="contained"
                    color="secondary"
                    sx={{mr: 4, width: 100}}
                    onClick={onBack}
                >
                    Back
                </Button>
                <Typography variant={"h2"}>
                    Select a board size
                </Typography>
            </Stack>

            <Stack sx={{mt: 4}} direction={"row"} justifyContent={"center"} flexWrap={"nowrap"}>
                {sizes.map((size) =>
                    <SizeSelectionCard
                        key={"size" + size}
                        size={size}
                    />
                )}
            </Stack>
        </Stack>
    )
}