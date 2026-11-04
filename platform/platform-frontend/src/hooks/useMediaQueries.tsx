import { useMediaQuery } from "@mui/material";
import {theme} from "../config/theme/theme.ts";

export function useMediaQueries(){
    const isSmallScreen = useMediaQuery(theme.breakpoints.down("md"));
    const isMediumScreen = useMediaQuery(theme.breakpoints.down("md"));
    return{
        isSmallScreen,
        isMediumScreen
    }
}
