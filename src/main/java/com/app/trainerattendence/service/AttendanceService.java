package com.app.trainerattendence.service;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    // ✅ Check-in
    public Attendance checkIn(String userId, String department, double latitude, double longitude) {
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setDepartment(department);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setCheckInLatitude(latitude);
        attendance.setCheckInLongitude(longitude);
        attendance.setDate(LocalDate.now());
        return attendanceRepository.save(attendance);
    }

    // ✅ Check-out with duration calculation
    public Attendance checkOut(String userId, double latitude, double longitude) {
        Attendance attendance = attendanceRepository.findTopByUserIdOrderByCheckInTimeDesc(userId);

        if (attendance != null && attendance.getCheckOutTime() == null) {
            attendance.setCheckOutTime(LocalDateTime.now());
            attendance.setCheckOutLatitude(latitude);
            attendance.setCheckOutLongitude(longitude);

            // Calculate duration between check-in and check-out
            Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            attendance.setDuration(hours + "h " + minutes + "m");

            return attendanceRepository.save(attendance);
        }
        return attendance;
    }

    // ✅ Get all attendance records
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // ✅ Get all records by a specific user
    public List<Attendance> getUserAttendance(String userId) {
        return attendanceRepository.findByUserId(userId);
    }

    // ✅ Get attendance within date range
    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }
}
