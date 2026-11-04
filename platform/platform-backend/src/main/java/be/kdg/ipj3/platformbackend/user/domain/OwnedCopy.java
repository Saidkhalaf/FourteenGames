package be.kdg.ipj3.platformbackend.user.domain;

import be.kdg.ipj3.platformbackend.game.domain.GameId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public class OwnedCopy {
    private OwnedCopyId id;
    private GameId gameId;
    @Setter
    private boolean isFavorite;
}
