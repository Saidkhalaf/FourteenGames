package be.kdg.ipj3.platformbackend.achievement.infrastructure.jpa;

import be.kdg.ipj3.platformbackend.achievement.domain.AchievementId;
import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievement;
import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievementId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaPlatformUserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_achievements")
public class JpaUserAchievementEntity {
    @Id
    private JpaUserAchievementId id;

    @Column
    private LocalDateTime dateAchieved;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private JpaPlatformUserEntity user;

    public JpaUserAchievementEntity() {
    }

    public JpaUserAchievementEntity(JpaUserAchievementId id, LocalDateTime dateAchieved, JpaPlatformUserEntity jpaUser) {
        this.id = id;
        this.dateAchieved = dateAchieved;
        this.user = jpaUser;
    }

    public static JpaUserAchievementEntity fromDomain(UserAchievement userAchievement, JpaPlatformUserEntity jpaUser) {
        return new JpaUserAchievementEntity(
                JpaUserAchievementId.fromDomain(userAchievement.id()),
                userAchievement.dateAchieved(),
                jpaUser
        );
    }

    public UserAchievement toDomain() {
        return new UserAchievement(
                new UserAchievementId(
                        new UserId(this.id.getUserId()),
                        new AchievementId(this.id.getAchievementId())
                ),
                this.dateAchieved
        );
    }
}
