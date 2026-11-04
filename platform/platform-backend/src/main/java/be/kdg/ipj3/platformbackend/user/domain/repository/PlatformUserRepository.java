package be.kdg.ipj3.platformbackend.user.domain.repository;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlatformUserRepository {
    Optional<PlatformUser> findUserById(UserId userId);
    List<PlatformUser> findAllUsersByIds(List<UUID> idList);
    void save(PlatformUser user);
    PlatformUser createUser(PlatformUser user);
    PlatformUser findUserByUserName(String userName);
    List<PlatformUser> findRecommendationsListOfSize(List<UUID> exlucdedIds, int size);
    List<PlatformUser> findRecommendationsListOfSizeWithNameQuery(List<UUID> excludedIds,String nameQuery, int size);
    List<PlatformUser> findByIdIn(Collection<UUID> ids);
    List<OwnedCopy> findFavoriteGames(UUID id);
    List<OwnedCopy> findOwnedGames(UUID id);
    Optional<OwnedCopy> findFavoriteGameByGameId(UUID userId, UUID gameId);
    Optional<OwnedCopy> findOwnedGameByGameId(UUID userId, UUID gameId);
}
