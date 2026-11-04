import type {User} from "../models/auth/user.ts";
import {create} from "zustand";
import {keycloak} from "../config/security";
import {isExpired} from "react-jwt";
import {addAccessTokenToAuthHeader, notifyBackendUserLogin, removeAccessTokenFromAuthHeader} from "../services/auth.ts";
import type {KeycloakTokenParsed} from "keycloak-js";
import {endConversationOnLogout} from "../services/conversationService.ts";
import {useSelectionStore} from "./selectionStore.ts";

interface SecurityState{
    isInitialised:boolean;
    loggedInUser:User | undefined;
}

interface SecurityActions{
    init:() => void;
    login:() => void;
    manageAccount:() => void;
    logout: () => void;
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
            notifyBackendUserLogin();
        };
        keycloak.onAuthLogout = () => {
            removeAccessTokenFromAuthHeader();
            endConversationOnLogout(useSelectionStore.getState().currentConversationId!);
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
    manageAccount:() => {
        keycloak.accountManagement();
    },
    logout: () => {
        endConversationOnLogout(useSelectionStore.getState().currentConversationId!);
        removeAccessTokenFromAuthHeader();
        set({loggedInUser: undefined});
        keycloak.logout({redirectUri: window.location.origin})
    },
    isAuthenticated: () => {
        if (keycloak.token) return !isExpired(keycloak.token);
        return false;
    },
    updateUserFromToken: () => {
        if(!keycloak.tokenParsed || !keycloak.idTokenParsed) return;

        const parsed = keycloak.tokenParsed as KeycloakTokenParsed;
        const idParsed = keycloak.idTokenParsed as KeycloakTokenParsed;

        const userId = parsed["sub"];
        const preferredUsername = parsed["preferred_username"];
        const email = parsed["email"];
        const givenName = idParsed["given_name"];
        const familyName = idParsed["family_name"];
        const realmRoles = parsed.realm_access?.roles ?? [];

        set({
            loggedInUser:{
                id:userId!,
                name: givenName ?? preferredUsername ?? "",
                username: preferredUsername,
                email,
                firstName: givenName,
                lastName: familyName,
                roles: realmRoles,
            }
        });

    },

}))