import {NavBarLink} from "./NavBarLink.tsx";
import {Stack} from "@mui/material";
import {useSecurityStore} from "../../stores/securityStore.ts";

interface NavbarLinksProps{
    isSmallScreen?:boolean;
}

//Searching for the actualy router config?
//This is found within ReactRouterConfig.tsx
//This file only controls the links to routes. NOt the routes itself.
export function NavbarLinks({isSmallScreen}:NavbarLinksProps){
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    return(
        <Stack direction={"column"}>
            <NavBarLink textContent={"Winkel"} linkTo={"/"} isSmallScreen={isSmallScreen}/>
            <NavBarLink textContent={"Bibliotheek"} linkTo={"/library"} requiresSignIn={true} isSmallScreen={isSmallScreen}/>
            <NavBarLink textContent={"Vrienden"} linkTo={"/friends"} requiresSignIn={true} isSmallScreen={isSmallScreen}/>
            <NavBarLink textContent={"Profiel"} linkTo={`/profile/${loggedInUser?.username}`} requiresSignIn={true} isSmallScreen={isSmallScreen}/>
            <NavBarLink textContent={"Achievements"} linkTo={"/achievements"} requiresSignIn={true} isSmallScreen={isSmallScreen}/>
        </Stack>
    )
}