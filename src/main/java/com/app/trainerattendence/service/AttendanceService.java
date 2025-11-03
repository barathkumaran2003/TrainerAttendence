package com.app.trainerattendence.service;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.AttendanceRepository;
import com.app.trainerattendence.repository.UserRepository;
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
    private final UserRepository userRepository;

    public Attendance checkIn(String userId, String department, double latitude, double longitude) {
        User user = userRepository.findByUserId(userId);

        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setUserName(user != null ? user.getName() : "Unknown"); // ✅ Show name
        attendance.setDepartment(department);
        attendance.setDate(LocalDate.now());
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setCheckInLatitude(latitude);
        attendance.setCheckInLongitude(longitude);

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(String userId, double latitude, double longitude) {
        Attendance attendance = attendanceRepository.findTopByUserIdOrderByCheckInTimeDesc(userId);

        if (attendance != null && attendance.getCheckOutTime() == null) {
            attendance.setCheckOutTime(LocalDateTime.now());
            attendance.setCheckOutLatitude(latitude);
            attendance.setCheckOutLongitude(longitude);

            // ✅ Calculate duration
            Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            attendance.setDuration(hours + "h " + minutes + "m");

            return attendanceRepository.save(attendance);
        }
        return null;
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getUserAttendance(String userId) {
        return attendanceRepository.findByUserId(userId);
    }

    public List<Attendance> getAttendanceByDateRange(LocalDate start, LocalDate end) {
        return attendanceRepository.findByDateBetween(start, end);
    }
}
