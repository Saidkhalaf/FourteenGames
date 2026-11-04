package be.kdg.ipj3.platformbackend.game.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Genre {

    private final String name;
    private final String description;

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static NotFoundException notFound() {
        log.error("Genre not found");
        return new NotFoundException("Genre not found");
    }
}
