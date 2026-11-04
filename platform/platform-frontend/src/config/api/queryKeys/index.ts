export {invalidateFriendRelatedKeys} from './invalidationFunctions';

export const gamesQueryKey={
    all:["gameList"] as const,
    currentGame: (gameId: string) => ["currentGame", gameId] as const,
}

export const ownedGamesQueryKeys={
    all:["ownedGames"] as const,
    currentGame: (gameId: string) => ["ownedGames", gameId] as const,
}

export const favoriteGamesQueryKey={
    all:["favoriteGames"] as const,
    currentGame: (gameId: string) => ["currentFavorite", gameId] as const,
}

export const currentUserDataQueryKey={
    current:["currentUserData"] as const,
}

export const plaformUsersDataQueryKey={
    all:["allUsersData"] as const,
}

export const friendsQueryKey={
    all:["friends"] as const,
}

export const friendRequestQueryKey={
    all:["friend-requests"] as const,
}

export const friendRecommendationsQueryKey={
    all:["friend-recommendations"] as const,
}

export const lobbyQueryKey={
    all:["lobbies"] as const,
    currentLobby: (lobbyId: string) => ["lobbies", lobbyId] as const,
    currentByGameId: (gameId: string) => ["lobbies","gameid", gameId] as const,
}

export const conversationQueryKey={
    current:["currentConversation"] as const,
}