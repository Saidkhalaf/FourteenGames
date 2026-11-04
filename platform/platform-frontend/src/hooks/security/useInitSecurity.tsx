import {useSecurityStore} from "../../stores/securityStore.ts";
import {useEffect} from "react";

export function useInitSecurity(){
    const init = useSecurityStore((s) => s.init);

    useEffect(() => {
        init();
    }, []);
}