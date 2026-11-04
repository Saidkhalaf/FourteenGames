package be.kdg.ipj3.tictactoebackend.infrastructure.gameState.jpa;

import be.kdg.ipj3.tictactoebackend.domain.*;
import be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa.JpaGameCharacterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor @Entity @Getter @Table(name = "game_state") public class JpaGameStateEntity {
    @Id private UUID id;
    @Column @Convert(converter = BoardConverter.class) private Cell[][] state;
    @ManyToOne() @JoinColumn(name = "player1_id")
    private JpaGameCharacterEntity player1;
    @ManyToOne() @JoinColumn(name = "player2_id")
    private JpaGameCharacterEntity player2;
    @Column private boolean isPlayerOneTurn;
    @Column private GameStatus status;
    @Column private UUID winner;
    @Column private boolean isAiGame;

    protected JpaGameStateEntity() {
    }

    public static JpaGameStateEntity fromDomain(GameState gameState, JpaGameCharacterEntity player1, JpaGameCharacterEntity player2) {
        return new JpaGameStateEntity(
                gameState.getId().id(),
                gameState.getBoard(),
                player1,
                player2,
                gameState.getIsPlayerOneTurn(),
                gameState.getStatus(),
                gameState.getWinner() == null ? null : gameState.getWinner().id(),
                gameState.isAiGame()
        );
    }

    public GameState toDomain() {
        return new GameState(
                new GameStateId(this.id),
                this.state,
                new CharacterId(this.player1.getId()),
                new CharacterId(this.player2.getId()),
                this.isPlayerOneTurn,
                this.status,
                new CharacterId(this.winner),
                this.isAiGame
        );
    }
}
