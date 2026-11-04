import {useMutation} from "@tanstack/react-query";
import {queryClient} from "../../../config/api";
import {conversationQueryKey} from "../../../config/api/queryKeys";
import {sendMessage, sendMessageWithoutUser} from "../../../services/conversationService.ts";
import {useSelectionStore} from "../../../stores/selectionStore.ts";
import {useSecurityStore} from "../../../stores/securityStore.ts";

export function useSendMessage() {
    const setCurrentConversationId = useSelectionStore((state) => state.setCurrentConversationId);
    const loggedInUser = useSecurityStore((state) => state.loggedInUser);
    const {mutateAsync, isPending, isError} = useMutation(
        {
            mutationFn: async ({conversationId, message}: {conversationId:string, message:string}) => {
                const currentPageUrl = window.location.href;
                if(loggedInUser){
                    return sendMessage(conversationId, {text:message, currentPageUrl:currentPageUrl, gameName:"",sentTime:new Date(Date.now())})
                }else{
                    return sendMessageWithoutUser(conversationId,  {text:message, currentPageUrl:currentPageUrl, gameName:"",sentTime:new Date(Date.now())});
                }
            },
            onSuccess: (data) => {
                setCurrentConversationId(data.id);
                queryClient.invalidateQueries({queryKey: conversationQueryKey.current});
            }
        }
    )

    return {
        sendMessage:mutateAsync,
        messagePending: isPending,
        messageError:isError,
    }
}