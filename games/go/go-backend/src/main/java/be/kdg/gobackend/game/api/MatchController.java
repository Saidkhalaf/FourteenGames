package be.kdg.gobackend.game.api;

import be.kdg.gobackend.game.api.dto.GameStateDto;
import be.kdg.gobackend.game.api.dto.NewAiMatchDto;
import be.kdg.gobackend.game.api.dto.NewMatchDto;
import be.kdg.gobackend.game.api.dto.NewStoneDto;
import be.kdg.gobackend.game.application.GameStateService;
import be.kdg.gobackend.game.domain.GameStateId;
import be.kdg.gobackend.game.domain.PlayerId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/go/api/matches")
@Slf4j
@RequiredArgsConstructor
public class MatchController {

    final GameStateService gameStateService;

    @PostMapping
    public ResponseEntity<GameStateDto> startGame(@AuthenticationPrincipal Jwt token, @RequestBody NewMatchDto newAiMatchDto){
        log.info("Starting a new game");
        final var player1Id = PlayerId.fromToken(token);
        final var player2Id = new PlayerId(newAiMatchDto.player2Id());
        final var state = gameStateService.startGame(player1Id,player2Id,newAiMatchDto.settings().boardSize());
        return ResponseEntity.ok(GameStateDto.from(state));
    }

    @PostMapping("/ai")
    public ResponseEntity<GameStateDto> startAiGame(@AuthenticationPrincipal Jwt token, @RequestBody NewAiMatchDto newAiMatchDto){
        log.info("Starting a new game vs AI");
        final var playerId = PlayerId.fromToken(token);
        final var state = gameStateService.startAiGame(playerId, newAiMatchDto.size());
        return ResponseEntity.ok(GameStateDto.from(state));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameStateDto> getGame(@PathVariable UUID id){
        log.info("Getting game with id {}", id);
        final var stateId = new GameStateId(id);
        final var state = gameStateService.getState(stateId);
        return ResponseEntity.ok(GameStateDto.from(state));
    }

    @GetMapping("/playing")
    public ResponseEntity<GameStateDto> getPlayingGame(@AuthenticationPrincipal Jwt token){
        final var playerId = PlayerId.fromToken(token);
        log.info("Getting playing game for user {}", playerId.id());
        final var state = gameStateService.getPlayingStateForPlayerAndState(playerId);
        return ResponseEntity.ok(GameStateDto.from(state));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GameStateDto> placeStone(@AuthenticationPrincipal Jwt token, @PathVariable UUID id, @RequestBody NewStoneDto stone){
        log.info("Player from match {} placing a stone at {}, {}", id, stone.x(), stone.y());
        final var stateId = new GameStateId(id);
        final var playerId = PlayerId.fromToken(token);
        final var state = gameStateService.placeStone(stateId, stone.x(), stone.y(), playerId);
        return ResponseEntity.ok(GameStateDto.from(state));
    }

    @PatchMapping("/{id}/ai")
    public ResponseEntity<GameStateDto> letAiPlaceStone(@AuthenticationPrincipal Jwt token, @PathVariable UUID id){
        log.info("Letting the AI place it's stone in match {}", id);
        final var stateId = new GameStateId(id);
        final var playerId = PlayerId.fromToken(token);
        final var state = gameStateService.letAiPlaceStone(stateId, playerId);
        return ResponseEntity.ok(GameStateDto.from(state));
    }

    @PatchMapping("/{id}/pass")
    public ResponseEntity<GameStateDto> passTurn(@AuthenticationPrincipal Jwt token, @PathVariable UUID id){
        log.info("pass in match {}", id);
        final var stateId = new GameStateId(id);
        final var playerId = PlayerId.fromToken(token);
        final var state = gameStateService.passTurn(stateId, playerId);
        return ResponseEntity.ok(GameStateDto.from(state));
    }
}
