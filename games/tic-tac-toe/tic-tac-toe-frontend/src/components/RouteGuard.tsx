import {type PropsWithChildren, useContext, useEffect} from 'react'
import SecurityContext from '../context/SecurityContext.ts'

export function RouteGuard({children}: PropsWithChildren) {
    const {isInitialised, isAuthenticated, login} = useContext(SecurityContext)

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
