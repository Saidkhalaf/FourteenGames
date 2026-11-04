import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Stack,
    TextField,
    ToggleButton,
    ToggleButtonGroup,
    useMediaQuery,
    useTheme
} from "@mui/material";
import {useState} from "react";
import {useAddCredits} from "../../hooks/api/user/useAddCredits.tsx";
import {creditName} from "../../config/theme/names.ts";

interface AddCreditsDialogProps {
    isOpen: boolean;
    onClose: () => void;
    presetValues: number[];
}

export function AddCreditsDialog({isOpen, onClose, presetValues}: AddCreditsDialogProps) {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("md"));
    const {addCredits} = useAddCredits();
    const [credits, setCredits] = useState<number | null>(null);

    const handleToggleChange = (_: unknown, value: number | null) => {
        if (value === null) return;
        setCredits(value);
    };
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = Number(e.target.value);
        if (Number.isNaN(value)) return;
        setCredits(value);
    };
    const handleSubmit = () => {
        if (!credits) return;
        addCredits(credits);
        setCredits(null);
        onClose();
    };
    return (
        <Dialog
            open={isOpen}
            onClose={onClose}
            fullWidth
            maxWidth="lg"
            fullScreen={isMobile}
        >
            <DialogTitle component="h4" color={theme.palette.primary.main}>
                {creditName} toevoegen
            </DialogTitle>

            <DialogContent >
                <Stack spacing={3} mt={1}>
                    <ToggleButtonGroup
                        exclusive
                        color="primary"
                        value={
                            presetValues.includes(credits ?? -1)
                                ? credits
                                : null
                        }
                        onChange={handleToggleChange}
                    >
                        {presetValues.map(value => (
                            <ToggleButton key={value} value={value.toFixed(2)} sx={{color:theme.palette.primary.main}}>
                                {value.toFixed(2)} credits
                            </ToggleButton>
                        ))}
                    </ToggleButtonGroup>

                    <TextField
                        label={`Aangepast aantal ${creditName}`}
                        type="number"
                        fullWidth
                        value={credits ?? ""}
                        onChange={handleInputChange}
                        sx={{
                            "& .MuiInputBase-input": {
                                color: theme.palette.primary.main,
                            },
                            "& .MuiOutlinedInput-root": {
                                borderRadius: 1,
                                "& fieldset": {
                                    borderWidth: 2,
                                    borderColor: theme.palette.primary.main,
                                },
                                "&:hover fieldset": {
                                    borderColor: theme.palette.primary.dark,
                                },
                                "&.Mui-focused fieldset": {
                                    borderColor: theme.palette.primary.main,
                                }
                            },
                        }}
                    />
                </Stack>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose} color="secondary">
                    Annuleren
                </Button>
                <Button
                    variant="contained"
                    onClick={handleSubmit}
                    disabled={!credits || credits < 1}
                >
                    Toevoegen
                </Button>
            </DialogActions>
        </Dialog>
    );
}
