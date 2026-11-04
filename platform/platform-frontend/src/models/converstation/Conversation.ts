import type {ChatMessage} from "./ChatMessage.ts";

export type Conversation = {
    id: string;
    userId: string;
    messages: ChatMessage[];
}