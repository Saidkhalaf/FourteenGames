package be.kdg.ipj3.platformbackend.user.infrastructure;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaOwnedCopyEntity;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaPlatformUserEntity;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.repository.JpaPlatformUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DbPlatformUserRepository implements PlatformUserRepository {

    private final JpaPlatformUserRepository jpaPlatformUserRepository;

    public DbPlatformUserRepository(JpaPlatformUserRepository jpaPlatformUserRepository) {
        this.jpaPlatformUserRepository = jpaPlatformUserRepository;
    }

    @Override
    public Optional<PlatformUser> findUserById(UserId userId) {
        return jpaPlatformUserRepository.findByIdWithAchievementsAndOwnedGames(userId.id()).map(JpaPlatformUserEntity::toDomain);
    }

    @Override
    public List<PlatformUser> findAllUsersByIds(List<UUID> idList) {
        return jpaPlatformUserRepository.findAllById(idList).stream()
                .map(JpaPlatformUserEntity::toDomain)
                .toList();
    }


    @Override
    public void save(PlatformUser user) {
        jpaPlatformUserRepository.save(JpaPlatformUserEntity.fromDomain(user));
    }


    @Override
    public PlatformUser createUser(PlatformUser user) {
        return jpaPlatformUserRepository.save(JpaPlatformUserEntity.fromDomain(user)).toDomain();
    }

    @Override
    public PlatformUser findUserByUserName(String userName) {
        return jpaPlatformUserRepository
                .findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User with username '" + userName + "' not found"))
                .toDomain();
    }

    @Override
    public List<PlatformUser> findRecommendationsListOfSize(List<UUID> excludedIds, int size) {
        return jpaPlatformUserRepository.findUsersNotInListLimited(excludedIds, size)
                .stream()
                .map(JpaPlatformUserEntity::toDomain)
                .toList();
    }

    @Override
    public List<PlatformUser> findRecommendationsListOfSizeWithNameQuery(List<UUID> excludedIds, String nameQuery, int size) {
        return jpaPlatformUserRepository.findUsersNotInListWithNameQuery(excludedIds, nameQuery, size)
                .stream()
                .map(JpaPlatformUserEntity::toDomain)
                .toList();
    }

    @Override
    public List<PlatformUser> findByIdIn(Collection<UUID> ids) {
        return jpaPlatformUserRepository.findByIdIn(ids).stream()
                .map(JpaPlatformUserEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnedCopy> findFavoriteGames(UUID id) {
        return jpaPlatformUserRepository.findFavoriteGames(id).stream()
                .map(JpaOwnedCopyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnedCopy> findOwnedGames(UUID id) {
        return jpaPlatformUserRepository.findOwnedGames(id).stream()
                .map(JpaOwnedCopyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OwnedCopy> findFavoriteGameByGameId(UUID userId, UUID gameId) {
        return jpaPlatformUserRepository.findFavoriteGameByGameId(userId, gameId)
                .map(JpaOwnedCopyEntity::toDomain);
    }

    @Override
    public Optional<OwnedCopy> findOwnedGameByGameId(UUID userId, UUID gameId) {
        return jpaPlatformUserRepository.findOwnedGameByGameId(userId, gameId)
                .map(JpaOwnedCopyEntity::toDomain);
    }

}