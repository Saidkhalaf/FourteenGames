import {Box, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, Divider, Stack, useMediaQuery, useTheme} from "@mui/material";
import {AddOnUserName} from "../controls/AddOnUserName.tsx";
import {FriendRecommendationList} from "../lists/FriendRecommendationList.tsx";
import {Suspense} from "react";

interface NewFriendDialogProps {
    isOpen: boolean;
    onClose: () => void;
}

export function NewFriendDialog({isOpen, onClose}: NewFriendDialogProps) {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));

    return (
        <Dialog
            open={isOpen}
            onClose={onClose}
            fullWidth
            maxWidth={"lg"}
            fullScreen={isMobile}
            sx={{color: theme.palette.primary.main}}
        >
            <DialogTitle sx={{color: theme.palette.primary.main}}
                         component={"h4"}>
                Nieuwe vrienden toevoegen
            </DialogTitle>
            <DialogContent>
                <Stack
                    direction={{ xs: "column", md: "row" }}
                    spacing={2}
                    sx={{ height: "100%" }}
                >
                    <Box sx={{ flex: { md: 3 }, width: "100%" }}>
                        <Suspense fallback={<CircularProgress/>}>
                            <FriendRecommendationList/>
                        </Suspense>
                    </Box>
                    <Divider
                        orientation="vertical"
                        flexItem
                        sx={{
                            display: { xs: "none", md: "block" },
                            borderColor: theme.palette.primary.main,
                            width: 2,
                            my: 2
                        }}
                    />

                    <Divider
                        orientation="horizontal"
                        flexItem
                        sx={{
                            display: { xs: "block", md: "none" },
                            borderColor: theme.palette.primary.main,
                        }}
                    />

                    <Box sx={{ flex: { md: 1 }, width: "100%" }}>
                        <AddOnUserName/>
                    </Box>
                </Stack>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}
                        color="secondary"
                >
                    Annuleren
                </Button>
            </DialogActions>
        </Dialog>
    )
}
