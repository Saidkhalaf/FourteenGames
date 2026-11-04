import {type PropsWithChildren,useEffect} from 'react'
import {useSecurityStore} from "../../stores/securityStore.ts";

export function RouteGuard({children}: PropsWithChildren) {
    const isAuthenticated = useSecurityStore((state) => state.isAuthenticated);
    const login = useSecurityStore((state) => state.login);
    const isInitialised = useSecurityStore((state) => state.isInitialised);

    useEffect(() => {
        if (isInitialised && !isAuthenticated()) {
            login()
        }
    }, [isAuthenticated, isInitialised, login])

    if (!isAuthenticated()) {
        // todo add a nice page loading for authentication
        return <div>Authenticating</div>
    }

    return children
}
