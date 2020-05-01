package org.inmediart.telegram.bot.repository;

import org.inmediart.telegram.bot.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByTelegramUserIdAndInProgressTrue(Integer telegramUserId);
}
