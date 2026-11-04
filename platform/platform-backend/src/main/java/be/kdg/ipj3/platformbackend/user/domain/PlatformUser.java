package be.kdg.ipj3.platformbackend.user.domain;

import be.kdg.ipj3.platformbackend.achievement.domain.AchievementId;
import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievement;
import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievementId;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.ForbiddenException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.GameAlreadyOwnedException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.InsufficientCreditsException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Slf4j
public class PlatformUser {
    private final UserId userId;

    private final String userName;
    private String biography;
    private List<UserAchievement> achievements = new ArrayList<>();
    private String profilePictureUrl;
    private String bannerUrl;
    private double credits;

    private final List<OwnedCopy> ownedGames;

    public PlatformUser(UserId userId, String userName, String biography, List<UserAchievement> achievements, String profilePictureUrl, String bannerUrl, double credits, List<OwnedCopy> ownedGames) {
        this.userId = userId;
        this.userName = userName;
        this.biography = biography;
        this.achievements = achievements != null ? achievements : new ArrayList<>();
        this.profilePictureUrl = profilePictureUrl;
        this.bannerUrl = bannerUrl;
        this.credits = credits;
        this.ownedGames = ownedGames;
    }

    public void unlockAchievement(AchievementId achievementId) {
        UserAchievementId newUAId = new UserAchievementId(userId, achievementId);

        if (achievements.stream().anyMatch(userAchievement -> userAchievement.id().equals(newUAId))) {
            log.info("User {} already has achievement {}", this.userId.id(), achievementId.id());
            return;
        }
        achievements.add(new UserAchievement(newUAId, LocalDateTime.now()));
        log.info("User {} has unlocked achievement {}", this.userId.id(), achievementId.id());
    }

    public void updateProfileDetails(String biography, String profilePictureUrl, String bannerUrl) {
        this.biography = biography;
        this.profilePictureUrl = profilePictureUrl;
        this.bannerUrl = bannerUrl;
    }

    public void addCredits(double credits) {
        this.credits += credits;
    }

    public void removeCredits(double credits) {
        this.credits -= credits;
    }

    public OwnedCopy buyGame(GameId gameId, double price) {
        if (price > this.credits) {
            log.error("User {} does not have enough credits", this.userId.id());
            throw new InsufficientCreditsException(credits, price);
        }
        if (ownedGames.stream().anyMatch(ownedCopy -> ownedCopy.getGameId().id().equals(gameId.id()))) {
            log.error("User {} allready owns game {}", this.userId.id(), gameId.id());
            throw new GameAlreadyOwnedException(gameId.id());
        }

        removeCredits(price);
        return addOwnedGame(gameId);
    }

    public OwnedCopy addOwnedGame(GameId gameId) {
        OwnedCopy oc = new OwnedCopy(new OwnedCopyId(UUID.randomUUID()), gameId, false);
        this.ownedGames.add(oc);
        return oc;
    }

    public void favoriteGame(OwnedCopyId ocId, boolean favorite) {
        this.ownedGames.stream()
                .filter(oc -> oc.getId().id().equals(ocId.id()))
                .findFirst().orElseThrow(ocId::notFound).setFavorite(favorite);
    }

    public void validateGameOwnerShip(GameId gameId) {
        if (this.ownedGames.stream()
                .noneMatch(ownedCopy -> ownedCopy.getGameId().id().equals(gameId.id()))) {
            log.info("user {} attempted to player {} but does not own a copy of this game.", this.userId.id(), gameId.id());
            throw new ForbiddenException(this.userId.id() + " does not own game " + gameId.id());
        }
    }

}
