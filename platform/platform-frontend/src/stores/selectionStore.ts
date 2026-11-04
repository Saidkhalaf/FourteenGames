import { create } from "zustand";

interface SelectionState {
    selectedGameId: string | null;
    currentConversationId:string|null;
}

interface SelectionActions {
    setSelectedGameId: (gameId: string | null) => void;
    setCurrentConversationId: (conversationId:string|null) => void;
}

export const useSelectionStore = create<SelectionState & SelectionActions>((set) => ({
    // State
    selectedGameId: null,
    currentConversationId:null,
    // Actions
    setSelectedGameId: (gameId: string|null) => set({ selectedGameId: gameId }),
    setCurrentConversationId: (conversationId: string|null) => set({ currentConversationId: conversationId }),
}));
