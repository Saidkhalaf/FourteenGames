package be.kdg.ipj3.tictactoebackend.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Slf4j
public record CharacterId(UUID id) {
    public CharacterId() {
        this(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        log.error("Character (player or npc) with id {} not found", id);
        return new NotFoundException("Character (player or npc) [" + id + "] not found");
    }

    public static CharacterId fromToken(Jwt token) {
        return new CharacterId(UUID.fromString(token.getClaimAsString("sub")));
    }
}
