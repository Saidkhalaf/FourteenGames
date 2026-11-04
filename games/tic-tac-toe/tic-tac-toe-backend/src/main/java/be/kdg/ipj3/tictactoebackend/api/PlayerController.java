package be.kdg.ipj3.tictactoebackend.api;

import be.kdg.ipj3.tictactoebackend.api.dtos.GameStateDtoChar;
import be.kdg.ipj3.tictactoebackend.application.GameStateService;
import be.kdg.ipj3.tictactoebackend.domain.CharacterId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tic-tac-toe/api/players")
@Slf4j
public class PlayerController {

    private final GameStateService gameStateService;

    public PlayerController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @GetMapping("/{id}/playing")
    public ResponseEntity<GameStateDtoChar> getGame(@PathVariable UUID id){
        log.info("Getting in_progress game per player {}", id);
        final var playerId = new CharacterId(id);
        final var state = gameStateService.getGameForPlayer(playerId);
        return ResponseEntity.ok(GameStateDtoChar.from(state));
    }
}
