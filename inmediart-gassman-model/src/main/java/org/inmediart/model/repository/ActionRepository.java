package org.inmediart.model.repository;

import org.inmediart.model.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByTelegramUserIdAndInProgressTrue(Integer telegramUserId);
}
