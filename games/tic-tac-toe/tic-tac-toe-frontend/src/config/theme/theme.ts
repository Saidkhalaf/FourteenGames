import {createTheme} from "@mui/material";
import {colors} from "./color.ts";

export const theme = createTheme({
    colorSchemes: {
        light: {
            palette: {
                mode: "light",
                primary: {
                    main: colors.white,
                    contrastText:colors.lightBlue,
                },
                secondary:{
                    main:colors.lightOrange,
                    contrastText:colors.white,
                },
                background: {
                    default: colors.white,
                },
                text:{
                    primary:colors.lightBlue
                }
            },
        },
        dark: {
            palette: {
                mode: "dark",
                primary: {
                    main: colors.darkBlue,
                    contrastText: colors.white,
                },
                secondary:{
                    main:colors.lightOrange,
                    contrastText:colors.white,
                },
                background: {
                    default: colors.lightBlue,
                },
                text:{
                    primary:colors.white
                }
            },
        },
    }
})
