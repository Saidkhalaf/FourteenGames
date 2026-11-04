import {useMutation} from "@tanstack/react-query";
import {queryClient} from "../../../config/api";
import {conversationQueryKey} from "../../../config/api/queryKeys";
import {startConversation, startConversationWithoutUser} from "../../../services/conversationService.ts";
import {useSelectionStore} from "../../../stores/selectionStore.ts";
import {useSecurityStore} from "../../../stores/securityStore.ts";

export function useStartConversation() {
    const setCurrentConversationId = useSelectionStore((state) => state.setCurrentConversationId);
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const {mutateAsync, isPending, isError} = useMutation(
        {
            mutationFn: async () => {
                if(loggedInUser){
                    return startConversation()
                }else{
                    return startConversationWithoutUser();
                }
            },
            onSuccess: (data) => {
                setCurrentConversationId(data.id);
                queryClient.invalidateQueries({queryKey: conversationQueryKey.current});
            }
        }
    )

    return {
        startConversation:mutateAsync,
        conversationPending: isPending,
        conversationError:isError,
    }
}