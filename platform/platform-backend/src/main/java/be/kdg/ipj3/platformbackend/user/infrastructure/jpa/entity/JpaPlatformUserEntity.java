package be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievement;
import be.kdg.ipj3.platformbackend.achievement.infrastructure.jpa.JpaUserAchievementEntity;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "platform_user")
@Access(AccessType.FIELD)
@Getter
public class JpaPlatformUserEntity {

    @Id
    @Column
    private UUID id;

    @Column
    private String userName;

    @Column
    private String biography;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaFriendRequestEntity> sentFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaFriendRequestEntity> receivedFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaUserAchievementEntity> achievements = new HashSet<>();
    @Column
    private String profilePictureUrl;

    @Column
    private String bannerUrl;

    @Column
    private double credits;

    @OneToMany(mappedBy = "user" ,orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<JpaOwnedCopyEntity> ownedGames;

    protected JpaPlatformUserEntity() {
    }

    private JpaPlatformUserEntity(UUID id, String userName, String biography, List<JpaFriendRequestEntity> friends, Set<JpaUserAchievementEntity> achievements, String profilePictureUrl, String bannerUrl, double credits) {
        this.id = id;
        this.userName = userName;
        this.biography = biography;
        this.receivedFriendRequests = friends;
        this.achievements = achievements;
        this.profilePictureUrl = profilePictureUrl;
        this.bannerUrl = bannerUrl;
        this.credits = credits;
    }

    public static JpaPlatformUserEntity fromDomain(PlatformUser domain) {
        JpaPlatformUserEntity entity = new JpaPlatformUserEntity();
        entity.id = domain.getUserId().id();
        entity.userName = domain.getUserName();
        entity.biography = domain.getBiography();
        entity.profilePictureUrl = domain.getProfilePictureUrl();
        entity.bannerUrl = domain.getBannerUrl();
        entity.credits = domain.getCredits();

        entity.achievements = domain.getAchievements().stream()
                .map(ua -> JpaUserAchievementEntity.fromDomain(ua, entity))
                .collect(Collectors.toSet());

        entity.ownedGames = domain.getOwnedGames().stream()
                .map(oc -> JpaOwnedCopyEntity.fromDomain(oc, entity))
                .collect(Collectors.toSet());
        return entity;
    }

    public PlatformUser toDomain() {
        List<UserAchievement> achievementList = achievements.stream()
                .map(JpaUserAchievementEntity::toDomain)
                .collect(Collectors.toList());
        List<OwnedCopy> ownedGamesDomain = this.ownedGames.stream()
                .map(JpaOwnedCopyEntity::toDomain)
                .collect(Collectors.toList());
        return new PlatformUser(new UserId(id), userName, biography, achievementList, profilePictureUrl, bannerUrl, credits, ownedGamesDomain);
    }
}