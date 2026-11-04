import {type FormEvent, useEffect, useState} from "react";
import {
    Box,
    SpeedDial,
    Paper,
    TextField,
    IconButton,
    Typography
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import SendIcon from "@mui/icons-material/Send";
import { chatBotName } from "../../config/chatbot";
import { BotMessage } from "./BotMessage.tsx";
import { UserMessage } from "./UserMessage.tsx";
import { useSelectionStore } from "../../stores/selectionStore.ts";
import { useConversation } from "../../hooks/api/conversation/useConversation.tsx";
import { useStartConversation } from "../../hooks/api/conversation/useStartConversation.tsx";
import { useSendMessage } from "../../hooks/api/conversation/useSendMessage.tsx";
import {useEndConversation} from "../../hooks/api/conversation/useEndConversation.tsx";
import ChatIcon from '@mui/icons-material/Chat';

export function ChatBox() {
    const [open, setOpen] = useState(false);
    const [inputValue, setInputValue] = useState("");
    const currentConversationId = useSelectionStore((state) => state.currentConversationId);
    const setCurrentConversationId = useSelectionStore((state) => state.setCurrentConversationId);
    const { conversation} = useConversation(currentConversationId!);
    const { startConversation } = useStartConversation();
    const { sendMessage } = useSendMessage();
    const { endConversation } = useEndConversation();

    useEffect(() => {
        if (open && currentConversationId === null) {
            startConversation();
        }
    }, [open, currentConversationId, startConversation]);

    useEffect(() => {
        const handleClose = async () => {
            if (!open && currentConversationId) {
                await endConversation(currentConversationId);
                setCurrentConversationId(null);
            }
        };
        handleClose();
    }, [open, currentConversationId, endConversation, setCurrentConversationId]);


    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (!inputValue.trim() || !currentConversationId) return;
        await sendMessage({
            conversationId: currentConversationId,
            message: inputValue.trim()
        });
        setInputValue("");
    };

    return (
        <Box
            sx={{
                position: "fixed",
                bottom: 16,
                right: 16,
                zIndex: 999
            }}
        >
            {open && (
                <Paper
                    elevation={6}
                    sx={{
                        position: "absolute",
                        bottom: 72,
                        right: 0,
                        width: 320,
                        height: 400,
                        display: "flex",
                        flexDirection: "column",
                        borderRadius: 4
                    }}
                >
                    <Box
                        sx={{
                            p: 1.5,
                            borderTopLeftRadius: 8,
                            borderTopRightRadius: 8,
                            bgcolor: "primary.main",
                            color: "white",
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center"
                        }}
                    >
                        <Typography variant="subtitle1">
                            {chatBotName} - support
                        </Typography>
                        <IconButton size="small" onClick={() => setOpen(false)}>
                            <CloseIcon sx={{ color: "white" }} />
                        </IconButton>
                    </Box>

                    <Box
                        sx={{
                            flex: 1,
                            p: 2,
                            overflowY: "auto",
                        }}
                    >
                        {conversation?.messages?.map((msg) =>
                            msg.sender != "00000000-0000-0000-0000-000000000001" ? (
                                <UserMessage key={msg.id} msg={msg.text} />
                            ) : (
                                <BotMessage key={msg.id} msg={msg.text} />
                            )
                        )}
                    </Box>

                    <Box
                        component="form"
                        onSubmit={handleSubmit}
                        sx={{
                            p: 1,
                            display: "flex",
                            gap: 1,
                            borderTop: "1px solid #ddd"
                        }}
                    >
                        <TextField
                            fullWidth
                            size="small"
                            placeholder="Type a message..."
                            value={inputValue}
                            onChange={(e) => setInputValue(e.target.value)}
                            slotProps={{
                                htmlInput: {
                                    maxLength: 255
                                }
                            }}
                        />
                        <IconButton color="primary" type="submit">
                            <SendIcon />
                        </IconButton>
                    </Box>
                </Paper>
            )}

            <SpeedDial
                ariaLabel="Chat"
                icon={<ChatIcon />}
                onClick={() => setOpen((prev) => !prev)}
                open={false}
                onMouseEnter={(e) => e.stopPropagation()}
                onMouseLeave={(e) => e.stopPropagation()}
                onFocus={(e) => e.stopPropagation()}
                onBlur={(e) => e.stopPropagation()}
            />
        </Box>
    );
}
