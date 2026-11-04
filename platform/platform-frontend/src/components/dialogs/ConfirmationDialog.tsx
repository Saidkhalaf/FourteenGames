import {Button, Dialog, DialogActions, DialogTitle, Typography} from "@mui/material";

interface ConfirmationDialogProps {
    confirmationMessage?: string,
    confirmationDescription?: string,
    warningMessage?: string,
    isOpen: boolean,
    onAccept: () => void,
    onClose: () => void,
    acceptButtonContent?: string,
    rejectButtonContent?:string
}

export function ConfirmationDialog({isOpen, onAccept, onClose, confirmationMessage= "Weet je het zeker?",confirmationDescription = "", warningMessage = "", acceptButtonContent = "ja", rejectButtonContent ="nee"}: ConfirmationDialogProps) {
    function handleAccept() {
        onAccept();
        onClose();
    }

    return (
        <Dialog open={isOpen}
                onClose={onClose}>
            <DialogTitle color={"primary"}>
                {confirmationMessage}
                <Typography color={"primary"}>
                    {confirmationDescription}
                </Typography>
                <Typography color={"secondary"} fontWeight={"bold"}>
                    {warningMessage}
                </Typography>
            </DialogTitle>
            <DialogActions>
                <Button
                    onClick={handleAccept}
                    variant={"contained"}
                    color={"primary"}
                >
                    {acceptButtonContent}
                </Button>
                <Button onClick={onClose}
                        color="secondary"
                >
                    {rejectButtonContent}
                </Button>
            </DialogActions>

        </Dialog>
    )
}
