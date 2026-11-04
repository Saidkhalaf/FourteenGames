package be.kdg.ipj3.platformbackend.achievement.infrastructure.jpa;

import be.kdg.ipj3.platformbackend.achievement.domain.Achievement;
import be.kdg.ipj3.platformbackend.achievement.domain.AchievementId;
import be.kdg.ipj3.platformbackend.game.infrastructure.jpa.entity.JpaGameEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "achievements")
public class JpaAchievementEntity {

    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private JpaGameEntity game;

    public JpaAchievementEntity() {
    }

    public JpaAchievementEntity(UUID id, String name, String description, JpaGameEntity game) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.game = game;
    }

    public static JpaAchievementEntity fromDomain(Achievement achievement, JpaGameEntity gameEntity){
        return new JpaAchievementEntity(
                achievement.getId().id(),
                achievement.getName(),
                achievement.getDescription(),
                gameEntity
        );
    }

    public Achievement toDomain(){
        return new Achievement(
                new AchievementId(this.id),
                this.name,
                this.description
        );
    }
}
