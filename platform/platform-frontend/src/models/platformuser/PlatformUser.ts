//To not get confused. This is the 'PlatformUser' The 'user' type is the 'JWT user'
import type {UserAchievement} from "../achievement/UserAchievement.ts";
import type {OwnedCopy} from "../game/OwnedCopy.ts";

export type PlatformUser = {
    userName: string
    biography: string
    achievements: UserAchievement[]
    profilePictureUrl:string,
    bannerUrl:string,
    credits: number,
    ownedCopies: OwnedCopy[]
}

export type MinimalPlatformUser = {
    id:string;
    userName: string
    biography: string
    profilePictureUrl:string
}


export type PlatformUserUpdateType = Omit<PlatformUser, "userName" | "achievements" | "credits"|"ownedCopies">