//This type should only be used within JWT auth context. For any other use case use 'platformuser'.
export type User = {
    id:string,
    name: string,
    username?: string,
    email?: string,
    firstName?: string,
    lastName?: string,
    roles: string[]
}