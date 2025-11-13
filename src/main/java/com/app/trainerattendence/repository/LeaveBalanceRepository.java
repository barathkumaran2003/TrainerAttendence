package com.app.trainerattendence.repository;

import com.app.trainerattendence.model.LeaveBalance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaveBalanceRepository extends MongoRepository<LeaveBalance, String> {
    LeaveBalance findByUserIdAndLeaveType(String userId, String leaveType);
    List<LeaveBalance> findByUserId(String userId);
}
