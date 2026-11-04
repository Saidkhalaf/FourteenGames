package be.kdg.ipj3.platformbackend.lobby.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.lobby.domain.Player;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "players")
public class JpaPlayerEntity {
    @Id
    @JoinColumn(name = "platform_user_id", nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private JpaLobbyEntity lobby;

    public Player toDomain() {
        return new Player(new UserId(userId));
    }

    public static JpaPlayerEntity fromDomain(Player player, JpaLobbyEntity lobby) {
        JpaPlayerEntity entity = new JpaPlayerEntity();
        entity.userId = player.userId().id();
        entity.lobby = lobby;
        return entity;
    }
}