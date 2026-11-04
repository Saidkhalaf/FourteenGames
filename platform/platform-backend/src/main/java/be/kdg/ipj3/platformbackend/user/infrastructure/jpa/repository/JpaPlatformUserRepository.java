package be.kdg.ipj3.platformbackend.user.infrastructure.jpa.repository;

import be.kdg.ipj3.platformbackend.achievement.infrastructure.jpa.JpaUserAchievementEntity;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaOwnedCopyEntity;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaPlatformUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface JpaPlatformUserRepository extends JpaRepository<JpaPlatformUserEntity, UUID> {

    Optional<JpaPlatformUserEntity> findByUserName(String userName);

    @Query("""
            SELECT u
            FROM JpaPlatformUserEntity u
            LEFT JOIN FETCH u.achievements
            LEFT JOIN FETCH u.ownedGames
            WHERE u.id = :id
            """)
    Optional<JpaPlatformUserEntity> findByIdWithAchievementsAndOwnedGames(UUID id);

    @Query(value = """
                SELECT *
                FROM platform_user u
                WHERE u.id NOT IN (:excludedIds)
                  AND LOWER(u.user_name) LIKE LOWER(CONCAT('%', :nameQuery, '%'))
                LIMIT :size
            """, nativeQuery = true)
    List<JpaPlatformUserEntity> findUsersNotInListWithNameQuery(
            @Param("excludedIds") List<UUID> excludedIds,
            @Param("nameQuery") String nameQuery,
            @Param("size") int size);

    @Query(value = """
                SELECT *
                FROM platform_user u
                WHERE u.id NOT IN (:excludedIds)
                LIMIT :size
            """, nativeQuery = true)
    List<JpaPlatformUserEntity> findUsersNotInListLimited(
            @Param("excludedIds") List<UUID> excludedIds,
            @Param("size") int size);

    @Query("""
            SELECT ua FROM JpaUserAchievementEntity ua WHERE ua.id.userId = :userId
            """)
    List<JpaUserAchievementEntity> findUserAchievementsByUserId(UUID userId);

    @Query("""
            SELECT og
            FROM JpaPlatformUserEntity u
            JOIN u.ownedGames og
            WHERE u.id = :userId
            AND og.isFavorite = true
            """)
    List<JpaOwnedCopyEntity> findFavoriteGames(UUID userId);

    @Query("""
            SELECT og
            FROM JpaPlatformUserEntity u
            JOIN u.ownedGames og
            WHERE u.id = :userId
            """)
    List<JpaOwnedCopyEntity> findOwnedGames(UUID userId);

    @Query("""
            SELECT og
            FROM JpaPlatformUserEntity u
            JOIN u.ownedGames og
            WHERE u.id = :userId
            AND og.isFavorite = true
            AND og.gameId = :gameId
            """)
    Optional<JpaOwnedCopyEntity> findFavoriteGameByGameId(UUID userId,UUID gameId);

    @Query("""
            SELECT og
            FROM JpaPlatformUserEntity u
            JOIN u.ownedGames og
            WHERE u.id = :userId
            AND og.gameId = :gameId
            """)
    Optional<JpaOwnedCopyEntity> findOwnedGameByGameId(UUID userId,UUID gameId);

    List<JpaPlatformUserEntity> findByIdIn(Collection<UUID> ids);
}
