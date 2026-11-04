import {useMutation, useQueryClient} from "@tanstack/react-query";
import {currentUserDataQueryKey} from "../../../config/api/queryKeys";
import {addCredits} from "../../../services/userService.ts";

export function useAddCredits() {
    const queryClient = useQueryClient();

    const {mutate, isPending, isError, isSuccess} = useMutation({
        mutationFn: (amount:number) => addCredits(amount),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: currentUserDataQueryKey.current
            });
        }
    })

    return {
        addCredits: mutate,
        isPending,
        isError,
        isSuccess,
    };
}
