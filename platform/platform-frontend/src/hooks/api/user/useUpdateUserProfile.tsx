import {useMutation, useQueryClient} from "@tanstack/react-query";
import type {PlatformUserUpdateType} from "../../../models/platformuser/PlatformUser.ts";
import {currentUserDataQueryKey} from "../../../config/api/queryKeys";
import {updateUserProfile} from "../../../services/userService.ts";

export function useUpdateUserProfile() {
    const queryClient = useQueryClient();

    const {mutate, isPending, isError, isSuccess} = useMutation({
        mutationFn: (userUpdateData: PlatformUserUpdateType) => updateUserProfile(userUpdateData),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: currentUserDataQueryKey.current
            });
        }
    })

    return {
        updateUserProfileData: mutate,
        isPending,
        isError,
        isSuccess,
    };
}
