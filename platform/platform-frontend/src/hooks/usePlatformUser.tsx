import {useSuspenseQuery} from "@tanstack/react-query";
import {currentUserDataQueryKey} from "../config/api/queryKeys";
import {getPlatformUserData} from "../services/userService.ts";
import {pollInterval} from "../config/realtime";

export function usePlatformUser(){
    const { data: platformUser} = useSuspenseQuery({
        queryKey: currentUserDataQueryKey.current,
        queryFn: () => getPlatformUserData(),
        refetchInterval:pollInterval
    })
    return {platformUser}
}