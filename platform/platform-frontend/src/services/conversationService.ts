import axios from "axios";
import type {Conversation} from "../models/converstation/Conversation.ts";
import type {ToChatbotMessage} from "../models/converstation/ToChatbotMessage.ts";

export async function getConversation(conversationId:string){
    const {data:conversation} = await axios.get<Conversation>(`/conversations/${conversationId}`)
    return conversation
}

export async function startConversation(){
    const {data:conversation} = await axios.post<Conversation>(`/conversations/start`)
    return conversation
}

export async function endConversation(conversationId:string){
    const {data:conversation} = await axios.delete<Conversation>(`/conversations/${conversationId}`)
    return conversation
}

export async function endConversationOnLogout(conversationId:string) {
    if(!conversationId)return;
    try {
        await axios.delete(`/conversations/${conversationId}`);
    } catch (e) {
        console.error("Failed to notify backend about user login", e);
    }
}

export async function sendMessage(conversationId:string, message:ToChatbotMessage){
    const {data:conversation} = await axios.post<Conversation>(`/conversations/${conversationId}/messages`,message)
    return conversation
}

export async function startConversationWithoutUser(){
    const {data:conversation} = await axios.post<Conversation>(`/conversations/visitor/start`)
    return conversation
}

export async function endConversationWithoutUser(conversationId:string){
    const {data:conversation} = await axios.delete<Conversation>(`/conversations/visitor/${conversationId}`)
    return conversation
}

export async function sendMessageWithoutUser(conversationId:string, message:ToChatbotMessage){
    const {data:conversation} = await axios.post<Conversation>(`/conversations/visitor/${conversationId}/messages`,message)
    return conversation
}




