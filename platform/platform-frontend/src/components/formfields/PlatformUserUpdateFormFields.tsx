import {TextField, useTheme} from "@mui/material";
import type {UpdateProfileFormValues} from "../../schemas/updateProfileSchema.ts";
import {useFormContext} from "react-hook-form";

export function PlatformUserUpdateFormFields() {
    const theme = useTheme();
    const {register, formState: {errors}} = useFormContext<UpdateProfileFormValues>();

    return (
        <>
            <TextField
                label="Biography"
                type="text"
                fullWidth
                sx={{
                    mb: 2,
                    "& .MuiInputBase-input": {
                        color: theme.palette.primary.main,
                    }
                }}
                error={!!errors.biography}
                helperText={errors.biography?.message}
                {...register("biography", {valueAsNumber: false})}
            />
            <TextField
                label="Profile picture (url)"
                type="text"
                fullWidth
                sx={{
                    mb: 2,
                    "& .MuiInputBase-input": {
                        color: theme.palette.primary.main,
                    }
                }}
                error={!!errors.profilePictureUrl}
                helperText={errors.profilePictureUrl?.message}
                {...register("profilePictureUrl", {valueAsNumber: false})}
            />
            <TextField
                label="Banner image (url)"
                type="text"
                fullWidth
                sx={{
                    mb: 2,
                    "& .MuiInputBase-input": {
                        color: theme.palette.primary.main,
                    }
                }}
                error={!!errors.bannerUrl}
                helperText={errors.bannerUrl?.message}
                {...register("bannerUrl", {valueAsNumber: false})}
            />
        </>
    )
}