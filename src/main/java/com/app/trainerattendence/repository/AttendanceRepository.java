package com.app.trainerattendence.repository;

import com.app.trainerattendence.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    // FIX: Find today's record only
    Attendance findByUserIdAndDate(String userId, LocalDate date);

    List<Attendance> findByUserId(String userId);

    List<Attendance> findByDateBetween(LocalDate start, LocalDate end);
}
