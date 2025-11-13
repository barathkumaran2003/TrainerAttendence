package com.app.trainerattendence.service;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService implements AttendanceServiceInterface {

    private final AttendanceRepository attendanceRepository;

    private LocalDate getTodayIST() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDate();
    }

    private LocalDateTime getNowIST() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }

    // -------------------- CHECK-IN --------------------
    @Override
    public Attendance checkIn(String userId, String userName, String department,
                              double latitude, double longitude, String address, boolean mode) {

        LocalDate today = getTodayIST();

        // ❗ Prevent multiple check-ins in same day
        Attendance existing = attendanceRepository.findByUserIdAndDate(userId, today);
        if (existing != null) {
            return existing; // Already checked-in today
        }

        Attendance attendance = new Attendance();

        attendance.setUserId(userId);
        attendance.setUserName(userName);
        attendance.setDepartment(department);

        attendance.setDate(today);

        attendance.setCheckInMode(mode);
        attendance.setCheckInTime(getNowIST());
        attendance.setCheckInLatitude(latitude);
        attendance.setCheckInLongitude(longitude);
        attendance.setCheckInAddress(address);

        attendance.setCheckOutMode(false); // default

        return attendanceRepository.save(attendance);
    }

    // -------------------- CHECK-OUT --------------------
    @Override
    public Attendance checkOut(String userId, double latitude, double longitude,
                               String address, boolean mode) {

        LocalDate today = getTodayIST();

        // ✔ Find today's record ONLY (fixes your problem)
        Attendance attendance = attendanceRepository.findByUserIdAndDate(userId, today);

        if (attendance == null) {
            return null; // No check-in today → cannot checkout
        }

        if (attendance.getCheckOutTime() != null) {
            return attendance; // Already checked out
        }

        attendance.setCheckOutMode(mode);
        attendance.setCheckOutTime(getNowIST());
        attendance.setCheckOutLatitude(latitude);
        attendance.setCheckOutLongitude(longitude);
        attendance.setCheckOutAddress(address);

        // duration
        Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        attendance.setDuration(hours + "h " + minutes + "m");

        return attendanceRepository.save(attendance);
    }

    // -------------------- GET ALL --------------------
    @Override
    public List<Attendance> getAllAttendance() {
        List<Attendance> list = attendanceRepository.findAll();
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<Attendance> getUserAttendance(String userId) {
        List<Attendance> list = attendanceRepository.findByUserId(userId);
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<Attendance> getAttendanceByDateRange(LocalDate start, LocalDate end) {
        List<Attendance> list = attendanceRepository.findByDateBetween(start, end);
        Collections.reverse(list);
        return list;
    }
}
