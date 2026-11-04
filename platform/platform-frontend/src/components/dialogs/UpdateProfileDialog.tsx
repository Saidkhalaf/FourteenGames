import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Stack, useTheme} from "@mui/material";
import {FormProvider, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {type UpdateProfileFormValues, updateProfileSchema} from "../../schemas/updateProfileSchema.ts";
import type {PlatformUserUpdateType} from "../../models/platformuser/PlatformUser.ts";
import {PlatformUserUpdateFormFields} from "../formfields/PlatformUserUpdateFormFields.tsx";
import {usePlatformUser} from "../../hooks/usePlatformUser.tsx";
import {ConfirmationDialog} from "./ConfirmationDialog.tsx";
import {useState} from "react";
import {useSyncReactHookForm} from "../../hooks/useSyncReactHookForm.tsx";

interface UpdateRoomDialogProps {
    isOpen: boolean,
    onClose: () => void,
    onUpdateUserProfile: (data: PlatformUserUpdateType) => void;
}


export function UpdateRoomDialog({isOpen, onClose, onUpdateUserProfile}: UpdateRoomDialogProps) {
    const [isConfirming, setIsConfirming] = useState(false);
    const {platformUser} = usePlatformUser();
    const theme = useTheme();
    const methods = useForm<UpdateProfileFormValues>({
        resolver: zodResolver(updateProfileSchema),
        defaultValues: {
            biography: platformUser.biography,
            profilePictureUrl: platformUser.profilePictureUrl,
            bannerUrl: platformUser.bannerUrl
        }
    })
    const {handleSubmit, reset, formState: {isSubmitting}} = methods;

    useSyncReactHookForm<UpdateProfileFormValues>({
        syncDependency: platformUser,
        reset: reset,
        values: {
            biography: platformUser.biography ?? "",
            profilePictureUrl: platformUser.profilePictureUrl ?? "",
            bannerUrl: platformUser.bannerUrl ?? "",
        },
    });

    function handleRoomUpdate(data: UpdateProfileFormValues) {
        const updatedUser: PlatformUserUpdateType = {
            biography: data.biography ? data.biography : "",
            profilePictureUrl: data.profilePictureUrl ? data.profilePictureUrl : "",
            bannerUrl: data.bannerUrl ? data.bannerUrl : "",
        }
        onUpdateUserProfile(updatedUser);
        onClose();
        reset();
    }

    function handleProfileReset(){
        const resetedUser: PlatformUserUpdateType={
            biography:"",
            profilePictureUrl:"",
            bannerUrl:""
        };
        onUpdateUserProfile(resetedUser);
        onClose();
        reset();
    }


    return (
        <>
            <Dialog open={isOpen}
                    onClose={onClose}
                    maxWidth={"lg"}
            >
                <DialogTitle sx={{color: theme.palette.primary.main}}>Profiel bijwerken</DialogTitle>
                <FormProvider {...methods}>
                    <form onSubmit={handleSubmit(handleRoomUpdate)}
                          noValidate>
                        <DialogContent>
                            <PlatformUserUpdateFormFields/>
                        </DialogContent>
                        <DialogActions
                            sx={{
                                display: "flex",
                                justifyContent: "space-between"
                            }}
                        >
                            <Button
                                variant={"contained"}
                                color={"secondary"}
                                sx={{ml: 2}}
                                onClick={() => setIsConfirming(true)}
                            >
                                Profiel resetten
                            </Button>
                            <Stack direction={"row"}
                                   gap={2}>
                                <Button type="submit"
                                        variant={"contained"}
                                        color={"primary"}
                                        disabled={isSubmitting}>
                                    Wijzigingen opslaan
                                </Button>
                                <Button onClick={onClose}
                                        color="secondary"
                                        disabled={isSubmitting}>Annuleren</Button>
                            </Stack>
                        </DialogActions>
                    </form>
                </FormProvider>
            </Dialog>
            <ConfirmationDialog
                confirmationMessage={"Ben je zeker dat je je profiel wilt resetten?"}
                confirmationDescription={"Dit zal je profiel in originele staat plaatsen"}
                warningMessage={"Dit zal niet ongedaan gemaakt kunnen worden."}
                isOpen={isConfirming}
                onAccept={handleProfileReset}
                onClose={() => setIsConfirming(false)}/></>
    )
}
