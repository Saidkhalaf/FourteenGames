export interface Notification {
    id:number;
    message: string;
    severity?: "success" | "info" | "warning" | "error";
}