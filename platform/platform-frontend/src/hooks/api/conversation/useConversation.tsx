import {useQuery} from "@tanstack/react-query";
import {getConversation} from "../../../services/conversationService.ts";
import {conversationQueryKey} from "../../../config/api/queryKeys";
import {chatInterval} from "../../../config/realtime";

export function useConversation(conversationId: string){
    const {data: conversation, isPending, isError,error} = useQuery({
        queryKey: conversationQueryKey.current,
        queryFn: () => getConversation(conversationId),
        enabled: conversationId != null,
        refetchInterval:chatInterval
    })
    return {conversation, isError, isPending,error}
}