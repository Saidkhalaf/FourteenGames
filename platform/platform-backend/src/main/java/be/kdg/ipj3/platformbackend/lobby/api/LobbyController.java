package be.kdg.ipj3.platformbackend.lobby.api;

import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.lobby.api.dtos.LobbyDto;
import be.kdg.ipj3.platformbackend.lobby.api.dtos.StartGameResponseDto;
import be.kdg.ipj3.platformbackend.lobby.api.dtos.request.LobbyCreationRequestDto;
import be.kdg.ipj3.platformbackend.lobby.api.dtos.request.StartGameRequest;
import be.kdg.ipj3.platformbackend.lobby.application.LobbyService;
import be.kdg.ipj3.platformbackend.lobby.domain.GameSessionId;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/{lobbyId}")
    public ResponseEntity<LobbyDto> findLobby(@PathVariable UUID lobbyId){
        LobbyDto lobbyDto = LobbyDto.from(lobbyService.findLobby(new LobbyId(lobbyId)));
        return ResponseEntity.ok(lobbyDto);
    }

    @GetMapping
    public ResponseEntity<List<LobbyDto>> findAllLobbies() {
        List<LobbyDto> lobbyDtos = lobbyService.findAllLobbies().stream().map(LobbyDto::from).toList();
        return ResponseEntity.ok(lobbyDtos);
    }

    @GetMapping("/byGame/{gameSessionId}")
    public ResponseEntity<LobbyDto> findLobbyByGameSessionId(@PathVariable UUID gameSessionId){
        GameSessionId sessionId = new GameSessionId(gameSessionId);
        Lobby lobby = lobbyService.findLobbyByGameId(sessionId);
        return ResponseEntity.ok(LobbyDto.from(lobby));
    }

    @PostMapping
    public ResponseEntity<LobbyDto> createNewLobby(@AuthenticationPrincipal Jwt token, @RequestBody LobbyCreationRequestDto requestDto) {
        UserId userId = UserId.fromToken(token);
        return ResponseEntity.ok(LobbyDto.from(lobbyService.createNewLobby(userId, 2, new GameId(requestDto.gameId()))));
    }

    @PatchMapping("/{lobbyId}")
    public ResponseEntity<LobbyDto> joinLobby(@AuthenticationPrincipal Jwt token, @PathVariable UUID lobbyId){
        UserId userId = UserId.fromToken(token);
        Lobby lobby = lobbyService.addPlayerToLobby(new LobbyId(lobbyId), userId);
        return ResponseEntity.ok(LobbyDto.from(lobby));
    }

    @DeleteMapping("/{lobbyId}")
    public ResponseEntity<String> leaveLobby(@AuthenticationPrincipal Jwt token, @PathVariable UUID lobbyId){
        UserId userId = UserId.fromToken(token);
        lobbyService.leaveLobby(userId, new LobbyId(lobbyId));
        return ResponseEntity.ok("Succesfully left the lobby.");
    }

    @PostMapping("/{lobbyId}/start")
    public ResponseEntity<StartGameResponseDto> startGame(
            @AuthenticationPrincipal Jwt token,
            @PathVariable UUID lobbyId,
            @RequestBody StartGameRequest requestData
            ){
        UserId userId = UserId.fromToken(token);
        UserId player2Id= new UserId(requestData.player2Id());
        UUID gameId = lobbyService.startGame(userId, player2Id, new LobbyId(lobbyId),token, requestData.settings());
        return ResponseEntity.ok(new StartGameResponseDto(gameId));
    }

    @PostMapping("/training/{gameUuid}")
    public ResponseEntity<String> startAiGame(
            @AuthenticationPrincipal Jwt token,
            @PathVariable UUID gameUuid
    ){
        final var gameId = new GameId(gameUuid);
        lobbyService.startTrainingGame(gameId, token);
        return ResponseEntity.ok("Game against Ai started succesfully.");
    }

}