import type {FriendRelation} from "./FriendRelation.ts";

export type FriendList = {
    id:string,
    friends:FriendRelation[]
};

export type FriendRequestList ={
    friends:FriendRelation[]
};

export type FriendRecommendationList = {
    recommendations:FriendRelation[]
};