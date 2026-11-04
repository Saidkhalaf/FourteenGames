import axios from 'axios'

export function addAccessTokenToAuthHeader(token: string | undefined) {
    if (token) axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    else {
        removeAccessTokenFromAuthHeader()
    }
}

export function removeAccessTokenFromAuthHeader() {
    delete axios.defaults.headers.common['Authorization']
}

export async function notifyBackendUserLogin() {
    try {
        await axios.post("/user");
    } catch (e) {
        console.error("Failed to notify backend about user login", e);
    }
}