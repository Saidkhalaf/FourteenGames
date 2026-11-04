import {useState} from "react";
import {Alert, Snackbar} from "@mui/material";

interface NotificationElementProps{
    message:string,
    severity?: "success" | "error" | "warning" | "info";
}


export function NotificationElement({ message, severity = "info" }: NotificationElementProps) {
    const [open, setOpen] = useState(true);
    return (
        <Snackbar
            open={open}
            autoHideDuration={3000}
            onClose={() => setOpen(false)}
            anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        >
            <Alert
                onClose={() => setOpen(false)}
                severity={severity}
                sx={{ width: "100%" }}
            >
                {message}
            </Alert>
        </Snackbar>
    );
}