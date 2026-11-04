import {createTheme} from "@mui/material";
import {colors} from "./color.ts";

export const theme = createTheme({
    colorSchemes: {
        light: {
            palette: {
                mode: "light",
                primary: {
                    main: colors.lightBlue,
                    dark:colors.white,
                    contrastText: colors.white,
                },
                secondary: {
                    main: colors.lightOrange,
                    contrastText: colors.white,
                },
                background: {
                    default: colors.lightBlue,
                    paper: colors.white,
                },
                info:{
                    main: colors.emerald,
                },
                text: {
                    primary: colors.black,
                    secondary: colors.darkBlue
                }
            },
        },
        dark: {
            palette: {
                mode: "dark",
                primary: {
                    main: colors.lightBlue,
                    dark:colors.darkBlue,
                    contrastText: colors.white,
                },
                secondary: {
                    main: colors.lightOrange,
                    contrastText: colors.white,
                },
                background: {
                    default: colors.lightBlue,
                    paper: colors.white,
                },
                info:{
                    main: colors.emerald,
                },
                text: {
                    primary: colors.white,
                    secondary: colors.darkBlue,
                }
            },
        },
    },
    components: {
        MuiAppBar: {
            styleOverrides: {
                root: {
                    boxShadow: "none",
                },
            },
        },
        MuiTableCell: {
            styleOverrides: {
                root: {
                    color: colors.lightBlue,
                    borderBottom: `1px solid ${colors.lightBlue}`,
                },
                head: {
                    color: colors.lightBlue,
                    fontWeight: 600,
                },
            },
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: {
                    color: colors.lightBlue,
                    '& .MuiOutlinedInput-notchedOutline': {
                        borderColor: colors.lightBlue,
                    },
                    '&:hover .MuiOutlinedInput-notchedOutline': {
                        borderColor: colors.lightBlue,
                    },
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: colors.lightBlue,
                    },
                },
            },
        },
        MuiSelect: {
            styleOverrides: {
                select: {
                    color: colors.lightBlue,
                },
                icon: {
                    color: colors.lightBlue,
                },
            },
        },
        MuiMenuItem: {
            styleOverrides: {
                root: {
                    color: colors.lightBlue,
                },
            },
        },
    }
})
