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

    public Attendance checkIn(String userId, String userName, String department,
                              double latitude, double longitude, String address,boolean mode) {

        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setUserName(userName);
        attendance.setDepartment(department);
        attendance.setMode(mode);
        attendance.setDate(LocalDate.now());
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setCheckInLatitude(latitude);
        attendance.setCheckInLongitude(longitude);
        attendance.setCheckInAddress(address);

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(String userId, String userName, String department,
            double latitude, double longitude, String address,boolean mode) {
        Attendance attendance = attendanceRepository.findTopByUserIdOrderByCheckInTimeDesc(userId);

        if (attendance != null && attendance.getCheckOutTime() == null) {
            attendance.setCheckOutTime(LocalDateTime.now());
            attendance.setCheckOutLatitude(latitude);
            attendance.setCheckOutLongitude(longitude);
            attendance.setCheckOutAddress(address);

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
