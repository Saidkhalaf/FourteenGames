import * as z from 'zod'

export const updateProfileSchema = z.object({
    biography: z.string().optional(),
    profilePictureUrl: z.url().optional().or(z.literal('')),
    bannerUrl : z.url().optional().or(z.literal(''))
})

export type UpdateProfileFormValues = z.infer<typeof updateProfileSchema>;
