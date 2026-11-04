import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";

interface FriendCardProps {
    userName: string;
}

export default function CurrentPlayerCard({userName}: FriendCardProps) {
    const navigate = useNavigate();
    return (
        <Button
            sx={{mt:2}}
            fullWidth
            variant="contained"
            color="secondary"
            onClick={() => navigate("/profile/" + userName)}
        >
            {userName}
        </Button>
    );
}