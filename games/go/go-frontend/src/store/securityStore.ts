import {create} from "zustand";
import {isExpired} from "react-jwt";
import {addAccessTokenToAuthHeader, removeAccessTokenFromAuthHeader} from "../services/auth.ts";
import type {KeycloakTokenParsed} from "keycloak-js";
import { keycloak } from "../config/security";
import type {User} from "../models/user.ts";


interface SecurityState{
    isInitialised:boolean;
    loggedInUser:User | undefined;
}

interface SecurityActions{
    init:() => void;
    login:() => void;
    isAuthenticated: () => boolean;
    updateUserFromToken: () => void;
}

export const useSecurityStore = create<SecurityState & SecurityActions>((set,get) => ({
    //State
    isInitialised: false,
    loggedInUser: undefined,
    //Actions
    init: () => {
        keycloak.init({onLoad: "check-sso"});
        keycloak.onReady = () => {
            set({isInitialised: true});
        };
        keycloak.onAuthSuccess = () => {
            addAccessTokenToAuthHeader(keycloak.token);
            get().updateUserFromToken();
        };
        keycloak.onAuthLogout = () => {
            removeAccessTokenFromAuthHeader();
            set({loggedInUser: undefined});
        };
        keycloak.onAuthError = () => {
            removeAccessTokenFromAuthHeader();
        };
        keycloak.onTokenExpired = () => {
            keycloak.updateToken(-1).then(() => {
                addAccessTokenToAuthHeader(keycloak.token);
                get().updateUserFromToken()
            })
        }
    },
    login:() => {
        keycloak.login();
    },
    isAuthenticated: () => {
        if (keycloak.token) return !isExpired(keycloak.token);
        return false;
    },
    updateUserFromToken: () => {
        if(!keycloak.tokenParsed || !keycloak.idTokenParsed) return;
        const parsed = keycloak.tokenParsed as KeycloakTokenParsed;

        const userId = parsed.sub
        const name = parsed.given_name
        const realmRoles =
            parsed.realm_access?.roles ?? []

        set({
            loggedInUser:{
                id:userId!,
                name,
                roles: realmRoles,
            }
        });

    },

}))