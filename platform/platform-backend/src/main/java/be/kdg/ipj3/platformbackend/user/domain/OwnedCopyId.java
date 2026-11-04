package be.kdg.ipj3.platformbackend.user.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import jakarta.persistence.Embeddable;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Slf4j
public record OwnedCopyId(UUID id) implements Serializable {

    public NotFoundException notFound() {
        log.error("Owned game with id {} not found.", id);
        return new NotFoundException("Owned Game [" + id + "] not found.");
    }

}
