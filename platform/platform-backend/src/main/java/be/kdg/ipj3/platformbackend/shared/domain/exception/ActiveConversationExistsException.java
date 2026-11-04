package be.kdg.ipj3.platformbackend.shared.domain.exception;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ActiveConversationExistsException extends RuntimeException {
    public ActiveConversationExistsException(UserId userId) {
        super("User already has an active conversation");
    }
}
