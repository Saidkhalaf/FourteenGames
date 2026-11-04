package be.kdg.ipj3.platformbackend.game.api;

import be.kdg.ipj3.platformbackend.game.api.dtos.FullGameDto;
import be.kdg.ipj3.platformbackend.game.api.dtos.GameListDto;
import be.kdg.ipj3.platformbackend.game.application.GameService;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService games;

    public GameController(GameService games) {
        this.games = games;
    }

    @GetMapping
    public ResponseEntity<List<GameListDto>> findAll(){
        List<GameListDto> dtos = games.findAll().stream().map(GameListDto::from).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullGameDto> findGame(@PathVariable final UUID id){
        Game game = games.find(new GameId(id));
        return ResponseEntity.ok(FullGameDto.from(game));
    }

}
