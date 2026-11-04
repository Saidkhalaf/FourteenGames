export const gameQueryKeys={
    current:["currentGame"] as const,
    currentWithGameId: (gameId: string) => ["currentGame", gameId] as const,
}

