import {create} from "zustand";

interface PlayerSessionState {
    currentGameId: string | null;
}

interface PlayerSessionActions {
    updateCurrentGameId: (id: string | null) => void;
}

export const useCurrentPlayerSessionStore = create<PlayerSessionState & PlayerSessionActions>((set) => ({
    //States
    currentGameId: null,
    //Sessions
    updateCurrentGameId: (id) => {
        set({currentGameId: id});
    },
}));