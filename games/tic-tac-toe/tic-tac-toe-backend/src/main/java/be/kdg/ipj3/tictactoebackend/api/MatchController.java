package be.kdg.ipj3.tictactoebackend.api;

import be.kdg.ipj3.tictactoebackend.api.dtos.GameStateDtoChar;
import be.kdg.ipj3.tictactoebackend.api.dtos.MoveDto;
import be.kdg.ipj3.tictactoebackend.application.GameStateService;
import be.kdg.ipj3.tictactoebackend.domain.GameStateId;
import be.kdg.ipj3.tictactoebackend.domain.CharacterId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tic-tac-toe/api/matches")
@Slf4j
public class MatchController {

    private final GameStateService gameStateService;

    public MatchController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @GetMapping("/playing")
    public ResponseEntity<GameStateDtoChar> getPlayingGame(@AuthenticationPrincipal Jwt token){
        final var playerId = CharacterId.fromToken(token);
        final var state = gameStateService.getGameForPlayer(playerId);
        return ResponseEntity.ok(GameStateDtoChar.from(state));
    }


    @PostMapping("/ai")
    public ResponseEntity<GameStateDtoChar> startGame(@AuthenticationPrincipal Jwt token){
        log.info("Starting a new game");
        final var playerId = CharacterId.fromToken(token);
        final var state = gameStateService.createBoardAi(playerId);
        return ResponseEntity.ok(GameStateDtoChar.from(state));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameStateDtoChar> getGame(@PathVariable UUID id){
        log.info("Getting game with id {}", id);
        final var stateId = new GameStateId(id);
        final var state = gameStateService.getState(stateId);
        return ResponseEntity.ok(GameStateDtoChar.from(state));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GameStateDtoChar> place(@RequestBody MoveDto move, @PathVariable UUID id, @AuthenticationPrincipal Jwt token){
        final var stateId = new GameStateId(id);
        final var playerId = CharacterId.fromToken(token);
        final var state = gameStateService.makeMove(move.x(), move.y(), playerId, stateId);
        return ResponseEntity.ok(GameStateDtoChar.from(state));
    }

    @PatchMapping("/{id}/ai")
    public ResponseEntity<GameStateDtoChar> letAiPlace(@PathVariable UUID id){
        final var stateId = new GameStateId(id);
        final var state = gameStateService.makeAiMove(stateId);
        return ResponseEntity.ok(GameStateDtoChar.from(state));
    }

}
