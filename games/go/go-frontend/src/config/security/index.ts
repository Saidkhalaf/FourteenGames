import Keycloak from "keycloak-js";

const keycloakConfig = {
    url: import.meta.env.VITE_KC_URL,
    realm: import.meta.env.VITE_KC_REALM,
    clientId: import.meta.env.VITE_KC_CLIENT_ID,
}

export const keycloak: Keycloak = new Keycloak(keycloakConfig)
