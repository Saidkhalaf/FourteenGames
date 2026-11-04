package be.kdg.ipj3.platformbackend.user.infrastructure.jpa.repository;

import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaFriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
import java.util.Optional;


public interface JpaFriendRepository extends JpaRepository<JpaFriendRequestEntity, UUID> {

    List<JpaFriendRequestEntity> findALlBySenderOrReceiverAndIsConfirmed(UUID senderId, UUID receiverId, boolean isConfirmed);
    List<JpaFriendRequestEntity> findAllBySenderAndIsConfirmed(UUID senderId,boolean isConfirmed);
    List<JpaFriendRequestEntity> findAllByReceiverAndIsConfirmed(UUID senderId, boolean isConfirmed);
    Optional<JpaFriendRequestEntity> findBySenderAndReceiverAndIsConfirmed(UUID senderId, UUID receiverId, boolean isConfirmed);
    Optional<JpaFriendRequestEntity> findBySenderAndReceiver(UUID senderId, UUID receiverId);

    @Query("SELECT f FROM JpaFriendRequestEntity f " +
            "WHERE ((f.sender = :userId AND f.receiver = :friendId) " +
            "   OR (f.sender = :friendId AND f.receiver= :userId)) " +
            "AND f.isConfirmed = false")
    Optional<JpaFriendRequestEntity> findPendingFriendRequest(@Param("userId") UUID userId,
                                                              @Param("friendId") UUID friendId);

    @Query("SELECT f FROM JpaFriendRequestEntity f " +
            "WHERE (f.sender = :userId " +
            "   OR (f.receiver = :userId)) " +
            "AND f.isConfirmed = true")
    List<JpaFriendRequestEntity> findAcceptedFriendRequestForUser(@Param("userId") UUID userId);

    @Query("""
    SELECT DISTINCT CASE
           WHEN f.sender = :userId THEN f.receiver
           ELSE f.sender
           END
    FROM JpaFriendRequestEntity f
    WHERE (:userId = f.sender OR :userId = f.receiver)
""")
    List<UUID> findFriendIdsByUserId(@Param("userId") UUID userId);

    void removeById(UUID id);
}