package org.inmediart.model.repository;

import org.inmediart.model.entity.RechargeUserCreditLog;
import org.inmediart.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RechargeUserCreditLogRepository extends JpaRepository<RechargeUserCreditLog, Long> {
    void deleteAllByUser(User user);
    List<RechargeUserCreditLog> findAllByUserOrderByRechargeDateTimeDesc(User user);
}
