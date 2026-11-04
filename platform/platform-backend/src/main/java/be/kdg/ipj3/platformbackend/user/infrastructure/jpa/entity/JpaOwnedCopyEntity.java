package be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopyId;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "owned_copies")
public class JpaOwnedCopyEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column
    private boolean isFavorite;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private JpaPlatformUserEntity user;

    public JpaOwnedCopyEntity() {
    }

    public JpaOwnedCopyEntity(UUID id, UUID gameId, boolean isFavorite, JpaPlatformUserEntity user) {
        this.id = id;
        this.gameId = gameId;
        this.isFavorite = isFavorite;
        this.user = user;
    }

    public static JpaOwnedCopyEntity fromDomain(OwnedCopy ownedCopy, JpaPlatformUserEntity user) {
        return new JpaOwnedCopyEntity(ownedCopy.getId().id(), ownedCopy.getGameId().id(), ownedCopy.isFavorite(), user);
    }

    public OwnedCopy toDomain() {
        return new OwnedCopy(new OwnedCopyId(id), new GameId(gameId), isFavorite);
    }
}
