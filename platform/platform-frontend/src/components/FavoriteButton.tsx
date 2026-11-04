import StarIcon from "@mui/icons-material/Star";
import StarOutlineIcon from "@mui/icons-material/StarOutline";
import {IconButton, useTheme} from "@mui/material";

interface FavoriteButtonProps{
    onClick: () => void;
    selected: boolean;
    mainColor?:boolean;
}

export function FavoriteButton({onClick, selected, mainColor=true}:FavoriteButtonProps){
    const theme = useTheme();
    return(
        <IconButton onClick={(e) => {
            e.stopPropagation();
            onClick();
        }}>
            {selected ?
                <StarIcon
                    sx={{color:mainColor? theme.palette.text.secondary: theme.palette.primary.contrastText}}
                    data-testid="favorite-icon"
                />
                :
                <StarOutlineIcon
                    sx={{color:mainColor? theme.palette.text.secondary: theme.palette.primary.contrastText}}
                    data-testid="not-favorite-icon"
                />
            }
        </IconButton>
    )
}