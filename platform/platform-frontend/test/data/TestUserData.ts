import type { User } from "../../src/models/auth/user.ts";
import type {FriendList} from "../../src/models/platformuser/FriendList.ts";
import type {FriendRelation} from "../../src/models/platformuser/FriendRelation.ts";

export const mockUserId = "11111111-1111-1111-1111-111111111111"

export const mockuser : User={
    id:"1",
    name:"testUser",
    username:"testUser",
    email:"test.user@email.be",
    firstName:"test",
    lastName:"user",
    roles:[]
}
export const mockFriendRelation : FriendRelation={
    userId:"1",
    userName:"testFriend1",
    biography:""
}
export const mockFriendRelation2 : FriendRelation={
    userId:"2",
    userName:"testFriend2",
    biography:""
}

export const mockFriendRequest : FriendRelation={
    userId:"3",
    userName:"testFriend3",
    biography:""
}
export const testFriendList : FriendList={
    id:mockUserId,
    friends:[mockFriendRelation,mockFriendRelation2]
}
export const emptyFriendList: FriendList={
 id:mockUserId,
 friends:[]
}

export const testFriendRequestList:FriendList={
    id:mockUserId,
    friends:[]
}
export const emptyFriendRequestList:FriendList={
    id:mockUserId,
    friends:[mockFriendRequest]
}