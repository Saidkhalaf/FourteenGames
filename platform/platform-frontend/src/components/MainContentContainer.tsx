import {Box, useTheme} from "@mui/material";
import {useMediaQueries} from "../hooks/useMediaQueries.tsx";

type AppLayoutProps = {
    children?: React.ReactNode;
};

export function MainContentContainer({children}: AppLayoutProps){
    const {isSmallScreen} = useMediaQueries();
    const theme = useTheme();
    return (
        <>
            <Box
                sx={{
                    flex: 7,
                    background: theme.palette.primary.dark,
                    borderTopLeftRadius:  isSmallScreen? 0 : 20,
                    p: 4,
                    pl: 6,
                }}
            >
                {children}
            </Box>
        </>
    )
}