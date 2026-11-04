package be.kdg.ipj3.platformbackend.game.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.achievement.domain.Achievement;
import be.kdg.ipj3.platformbackend.achievement.infrastructure.jpa.JpaAchievementEntity;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "games")
public class JpaGameEntity {

    @Id
    @Column
    private UUID id;

    @Column
    private String name;
    @Column
    private int maxPlayerCount;

    @Column
    private String description;

    @Column
    private double price;

    @Column
    private String image;

    @Column
    private String icon;

    @ManyToOne
    @JoinColumn(name = "genre_name", nullable = false)
    private JpaGenreEntity genre;

    @Column
    private String url;

    @OneToMany(mappedBy = "game" ,orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JpaAchievementEntity> achievements;

    @Column
    private String aiGameStartEndpoint;

    @Column
    private String gameStartEndpoint;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column
    private Map<String, Object> gameSettings;

    public JpaGameEntity() {
    }

    public JpaGameEntity(UUID id, String name,int maxPlayerCount, String description, double price, String image, String icon, String url, JpaGenreEntity genre, String aiGameStartEndPoint, String gameStartEndpoint,Map<String, Object> gameSettings) {
        this.id = id;
        this.name = name;
        this.maxPlayerCount = maxPlayerCount;
        this.description = description;
        this.price = price;
        this.image = image;
        this.icon = icon;
        this.url = url;
        this.genre = genre;
        this.aiGameStartEndpoint = aiGameStartEndPoint;
        this.gameStartEndpoint = gameStartEndpoint;
        this.gameSettings = gameSettings;
    }

    public static JpaGameEntity fromDomain(Game game) {
        JpaGameEntity entity = new JpaGameEntity(
                game.getId().id(),
                game.getName(),
                game.getMaxPlayerCount(),
                game.getDescription(),
                game.getPrice(),
                game.getImage(),
                game.getIcon(),
                game.getUrl(),
                JpaGenreEntity.fromDomain(game.getGenre()),
                game.getAiGameStartEndpoint(),
                game.getGameStartEndpoint(),
                game.getGameSettings()
        );
        entity.achievements = game.getAchievements().stream()
                .map(achievement -> JpaAchievementEntity.fromDomain(achievement, entity))
                .toList();

        return entity;
    }

    public Game toDomain() {
        List<Achievement> domainAchievements = new ArrayList<>();
        for (JpaAchievementEntity jpaAchievement : this.achievements){
          domainAchievements.add(jpaAchievement.toDomain());
        }

        return new Game(
                new GameId(this.id),
                this.name,
                this.maxPlayerCount,
                this.description,
                this.price,
                this.image,
                this.icon,
                this.url,
                this.genre.toDomain(),
                domainAchievements,
                this.aiGameStartEndpoint,
                this.gameStartEndpoint,
                this.gameSettings
        );
    }
}
