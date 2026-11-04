import type {Achievement} from "../achievement/Achievement.ts";

export type Game = {
    id: string;
    name: string;
    description: string;
    price: number;
    image: string;
    icon: string;
    genre:string;
    url:string;
    achievements: Achievement[],
    configurableSettings: Record<string, unknown>
}

export type CompactGame = Omit<Game,  "description" | "image">