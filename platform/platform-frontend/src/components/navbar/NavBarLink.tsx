import {Link} from "react-router-dom";
import {Typography, useTheme} from "@mui/material";
import {useSecurityStore} from "../../stores/securityStore.ts";

interface NavBarLinkProps {
    textContent: string;
    linkTo: string;
    isSmallScreen?:boolean;
    requiresSignIn?: boolean;
}

export function NavBarLink({textContent, linkTo,isSmallScreen=true, requiresSignIn = false}: NavBarLinkProps) {
    const isAuthenticated = useSecurityStore((state) => state.isAuthenticated);
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const theme = useTheme();
    return (
        <>

            {(!requiresSignIn || (isAuthenticated() && loggedInUser)) && (
                <Typography
                    sx={{
                        textDecoration: "none",
                        color: !isSmallScreen ? theme.palette.primary.contrastText : theme.palette.primary.main,
                        m: !isSmallScreen ? 2 : "initial",
                        mt: !isSmallScreen ? 0 : "initial",
                    }}
                    variant="h6"
                    component={Link}
                    to={linkTo}
                >
                    {textContent}
                </Typography>
            )}
        </>
    )
}
