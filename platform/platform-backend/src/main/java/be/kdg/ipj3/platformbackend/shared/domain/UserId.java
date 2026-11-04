package be.kdg.ipj3.platformbackend.shared.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Slf4j
public record UserId(UUID id) {
    public NotFoundException notFound() {
        log.error("User with id {} not found", id);
        return new NotFoundException("User [" + id + "] not found");
    }

    public static UserId fromToken(Jwt token) {
        return new UserId(UUID.fromString(token.getClaimAsString("sub")));
    }
}
