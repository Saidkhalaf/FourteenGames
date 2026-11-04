import { create } from "zustand";
import { v4 as uuidv4 } from 'uuid';

export interface Notification {
    id: string;
    message: string;
    severity?: "success" | "info" | "warning" | "error";
}

interface NotificationState {
    notifications: Notification[];
}

interface NotificationActions {
    addNotification: (notification: Omit<Notification, "id">) => void;
    removeNotification: (id: string) => void;
}

//This store is used inside the "NotificationStack component" simply calling addNotification will handle the notify of a user.
export const useNotificationStore = create<NotificationState & NotificationActions>((set) => ({
    // State
    notifications: [],

    // Actions
    addNotification: (notification) => {
        const id = uuidv4();
        set((state) => ({
            notifications: [...state.notifications, { id, ...notification }]
        }));
    },

    removeNotification: (id) => {
        set((state) => ({
            notifications: state.notifications.filter((n) => n.id !== id)
        }));
    },
}));