package be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGameCharacterRepository extends JpaRepository <JpaGameCharacterEntity, UUID> {
}
