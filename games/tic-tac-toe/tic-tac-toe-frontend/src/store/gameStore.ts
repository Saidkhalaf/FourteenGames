import {create} from "zustand";

interface PlayerSessionState {
    currentGameId: string | null;
    currentPlayerId: string |null;
}

interface PlayerSessionActions {
    updateCurrentGameId: (id: string | null) => void;
    updateCurrentPlayerId: (id:string | null) => void;
}

export const useCurrentPlayerSessionStore = create<PlayerSessionState & PlayerSessionActions>((set) => ({
    //States
    currentGameId: null,
    currentPlayerId: null,
    //Sessions
    updateCurrentGameId: (id) => {
        set({currentGameId: id});
    },
    updateCurrentPlayerId: (id) => {
        set({currentPlayerId: id});
    }
}));