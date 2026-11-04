package be.kdg.ipj3.platformbackend.lobby.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.infrastructure.jpa.entity.JpaGameEntity;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.lobby.domain.Player;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "lobbies")
public class JpaLobbyEntity {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private JpaGameEntity game;

    private UUID currentGameSessionId;

    private LocalDateTime creationDate;
    private int maxPlayerCount;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaPlayerEntity> players;

    public Lobby toDomain(){
        List<Player> domainPlayers = this.players.stream().map(JpaPlayerEntity::toDomain).collect(Collectors.toList());
        return new Lobby(new LobbyId(id),game.toDomain().getId(),this.currentGameSessionId, domainPlayers,this.creationDate, this.maxPlayerCount );
    }

    public static JpaLobbyEntity fromDomain(Lobby domain, Game game) {
        JpaLobbyEntity entity = new JpaLobbyEntity();
        entity.id = domain.getId().id();
        entity.creationDate = domain.getCreationDate();
        entity.maxPlayerCount = game.getMaxPlayerCount();
        entity.game = JpaGameEntity.fromDomain(game);
        entity.currentGameSessionId = domain.getCurrentGameSessionId();

        entity.players = domain.getPlayers()
                .stream()
                .map(player -> JpaPlayerEntity.fromDomain(player,entity))
                .collect(Collectors.toList());

        return entity;
    }

}