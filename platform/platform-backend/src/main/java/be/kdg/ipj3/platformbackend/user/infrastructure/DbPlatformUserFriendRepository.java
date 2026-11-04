package be.kdg.ipj3.platformbackend.user.infrastructure;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.ConflictException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserFriendRepository;
import be.kdg.ipj3.platformbackend.user.domain.PlatformFriendRequest;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.entity.JpaFriendRequestEntity;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.repository.JpaFriendRepository;
import be.kdg.ipj3.platformbackend.user.infrastructure.jpa.repository.JpaPlatformUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DbPlatformUserFriendRepository implements PlatformUserFriendRepository {

    private final JpaPlatformUserRepository jpaPlatformUserRepository;
    private final JpaFriendRepository jpaFriendRepository;

    public DbPlatformUserFriendRepository(JpaPlatformUserRepository jpaPlatformUserRepository, JpaFriendRepository jpaFriendRepository) {
        this.jpaPlatformUserRepository = jpaPlatformUserRepository;
        this.jpaFriendRepository = jpaFriendRepository;
    }

    @Override
    public PlatformFriendRequest save(PlatformFriendRequest request) {
        return jpaFriendRepository.save(JpaFriendRequestEntity.fromDomain(request, request.getSender().id(), request.getReceiver().id())).toDomain();
    }

    @Override
    public PlatformFriendRequest findFriendRequestBetween(UUID friendId, UserId userId) {
        jpaPlatformUserRepository.findById(friendId).orElseThrow(userId::notFound);
        return jpaFriendRepository
                .findBySenderAndReceiverAndIsConfirmed(userId.id(), friendId, false)
                .or(() -> jpaFriendRepository.findBySenderAndReceiverAndIsConfirmed(friendId, userId.id(), false))
                .orElseThrow(() -> new NotFoundException("Friend request not found"))
                .toDomain();
    }

    @Override
    public void remove(UserId userId, UserId friendId, boolean isConfirmed) {
        JpaFriendRequestEntity friendRelation = jpaFriendRepository.findBySenderAndReceiverAndIsConfirmed(userId.id(), friendId.id(), isConfirmed)
                .or(() -> jpaFriendRepository.findBySenderAndReceiverAndIsConfirmed(friendId.id(), userId.id(), isConfirmed))
                .orElseThrow(() -> new NotFoundException("Friend request not found"));

        jpaFriendRepository.delete(friendRelation);
    }

    @Override
    public List<PlatformFriendRequest> findAllFriendRequestsForUser(UserId userId) {
        return jpaFriendRepository
                .findAllByReceiverAndIsConfirmed(userId.id(), false)
                .stream()
                .map(JpaFriendRequestEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void validateIfFriendRelationExists(UserId userId, UserId friendId) {
        jpaFriendRepository.findBySenderAndReceiver(userId.id(), friendId.id())
                .ifPresent(match -> {
                    throw new ConflictException("There already is a relation between " + userId.id() + " and " + friendId.id());
                });
    }

    @Override
    public List<UUID> getUniqueFriendIdsForUser(UUID userId) {
        return jpaFriendRepository.findFriendIdsByUserId(userId);
    }

    @Override
    public List<PlatformFriendRequest> findAllFriendsForUser(UserId id) {
        return jpaFriendRepository.findAcceptedFriendRequestForUser(id.id())
                .stream()
                .map(JpaFriendRequestEntity::toDomain)
                .collect(Collectors.toList());
    }
}
