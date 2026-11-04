import {useQuery, } from "@tanstack/react-query";
import {plaformUsersDataQueryKey} from "../../../config/api/queryKeys";
import {getMultipleUsersPlatformUserData} from "../../../services/userService.ts";


export function usePlatformUsers(
    userIds: string[],
) {
    const { data: userData, isError, isPending } = useQuery({
        queryKey: [...plaformUsersDataQueryKey.all, userIds],
        queryFn: () => getMultipleUsersPlatformUserData(userIds),
    });

    return { userData, isPending, isError };
}