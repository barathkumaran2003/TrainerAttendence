package com.app.trainerattendence.repository;

import com.app.trainerattendence.model.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {
    List<LeaveRequest> findByUserId(String userId);
    List<LeaveRequest> findByStatusAndStartDateBetween(String status, LocalDate start, LocalDate end);
}
