package be.kdg.ipj3.platformbackend.user.application;

import be.kdg.ipj3.platformbackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.ipj3.platformbackend.analytics.messages.PaymentMadeMessage;
import be.kdg.ipj3.platformbackend.analytics.messages.PurchaseMadeMessage;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages.AchievementMessageDto;
import be.kdg.ipj3.platformbackend.achievement.domain.AchievementId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.api.dtos.UpdateUserProfileRequestDto;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PlatformUserRepository platformUserRepository;
    private final GameRepository gameRepository;
    private final AnalyticsMessagePublisher analyticsMessagePublisher;

    public PlatformUser addUser(UserId userId, String userName) {
        final var user = new PlatformUser(userId, userName, "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
        return platformUserRepository.createUser(user);
    }

    public List<PlatformUser> findUserListByIdList(List<UserId> friendIds) {
        final var uuids = friendIds.stream()
                .map(UserId::id)
                .toList();

        return platformUserRepository.findAllUsersByIds(uuids);
    }

    public PlatformUser findUserById(UserId userId) {
        return platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
    }

    public PlatformUser findOrCreateUserById(UserId userId, String userName) {
        return platformUserRepository.findUserById(userId)
                .orElseGet(() -> this.addUser(userId, userName));
    }

    public PlatformUser findUserByUserName(String userName) {
        return platformUserRepository.findUserByUserName(userName);
    }

    public PlatformUser unlockAchievement(AchievementMessageDto dto) {
        final var user = findUserById(new UserId(dto.userId()));
        user.unlockAchievement(new AchievementId(dto.achievementId()));
        platformUserRepository.save(user);
        return user;
    }

    public PlatformUser updateUserProfile(UserId userId, UpdateUserProfileRequestDto request) {
        final var user = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        user.updateProfileDetails(request.biography(), request.profilePictureUrl(), request.bannerUrl());
        platformUserRepository.save(user);
        return user;
    }

    public Game addFavoriteGame(UserId userId, GameId gameId) {
        log.info("User: " + userId + " added game with id" + gameId.id() + "to their favorites.");
        final var game = gameRepository.findById(gameId.id()).orElseThrow(gameId::notFound);
        final var user = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        final var oc = platformUserRepository.findOwnedGameByGameId(userId.id(), gameId.id()).orElseThrow(gameId::notFound);
        user.favoriteGame(oc.getId(), true);
        platformUserRepository.save(user);
        return game;
    }

    public void removeFavoriteGame(UserId userId, GameId gameId) {
        log.info("User: " + userId + " removed game with id" + gameId.id() + "from their favorites.");
        gameRepository.findById(gameId.id()).orElseThrow(gameId::notFound);
        final var user = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        final var oc = platformUserRepository.findOwnedGameByGameId(userId.id(), gameId.id()).orElseThrow(gameId::notFound);
        user.favoriteGame(oc.getId(), false);
        platformUserRepository.save(user);
    }

    public List<OwnedCopy> findAllFavoriteGames(UserId id) {
        log.info("Finding all favorite games of user {}", id);
        return platformUserRepository.findFavoriteGames(id.id());
    }

    public OwnedCopy findFavoriteGameByGameId(UserId userId, GameId gameId) {
        log.info("Finding favorite game {} of user {}", gameId.id(), userId.id());
        platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        return platformUserRepository.findFavoriteGameByGameId(userId.id(), gameId.id()).orElseThrow(gameId::notFound);
    }

    public PlatformUser addCredit(UserId userId, double amount) {
        log.info("Adding credit amount of {} to user {}", amount, userId.id());
        final var user = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        user.addCredits(amount);
        platformUserRepository.save(user);
        analyticsMessagePublisher.publishPaymentMadeMessage(new PaymentMadeMessage(userId.id(), amount));
        return user;
    }

    public List<OwnedCopy> findAllOwnedCopies(UserId userId) {
        log.info("Finding owned games of user {}", userId.id());
        return platformUserRepository.findOwnedGames(userId.id());
    }

    public OwnedCopy buyGame(UserId userId, GameId gameId) {
        log.info("User {} buying Game {}", userId.id(), gameId.id());
        final var user = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        final var game = gameRepository.findById(gameId.id()).orElseThrow(gameId::notFound);
        final var oc = user.buyGame(gameId,game.getPrice());
        platformUserRepository.save(user);
        analyticsMessagePublisher.publishPurchaseMadeMessage(new PurchaseMadeMessage(userId.id(), gameId.id(), game.getName(), game.getPrice()));
        return oc;
    }
}
