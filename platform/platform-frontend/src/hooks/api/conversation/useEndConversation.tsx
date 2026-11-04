import {useMutation} from "@tanstack/react-query";
import {queryClient} from "../../../config/api";
import {conversationQueryKey} from "../../../config/api/queryKeys";
import {endConversation, endConversationWithoutUser} from "../../../services/conversationService.ts";
import {useSelectionStore} from "../../../stores/selectionStore.ts";
import {useSecurityStore} from "../../../stores/securityStore.ts";

export function useEndConversation() {
    const setCurrentConversationId = useSelectionStore((state) => state.setCurrentConversationId);
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const {mutateAsync, isPending, isError} = useMutation(
        {
            mutationFn: async (conversationId: string) => {
                if(loggedInUser){
                    return endConversation(conversationId)
                }else{
                    return endConversationWithoutUser(conversationId);
                }
            },
            onSuccess: (data) => {
                setCurrentConversationId(data.id);
                queryClient.invalidateQueries({queryKey: conversationQueryKey.current});
            }
        }
    )

    return {
        endConversation:mutateAsync,
        endConversationPending: isPending,
        endConversationError:isError,
    }
}