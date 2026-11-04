import {useEffect} from "react";
import {useSecurityStore} from "../../store/securityStore.ts";

export function useInitSecurity(){
    const init = useSecurityStore((s) => s.init);

    useEffect(() => {
        init();
    }, []);
}