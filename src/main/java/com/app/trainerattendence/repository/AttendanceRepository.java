package com.app.trainerattendence.repository;

import com.app.trainerattendence.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    // ✅ Find the latest check-in record for a user
    Attendance findTopByUserIdOrderByCheckInTimeDesc(String userId);

    // ✅ Get all attendance records for a user
    List<Attendance> findByUserId(String userId);

    // ✅ Get attendance records within a specific date range
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
