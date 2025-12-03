package com.app.trainerattendence.service;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
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

    @Override
    public String checkIn(String userId, String userName, String department,
                          double latitude, double longitude, String address, boolean mode) {

        LocalDate today = getTodayIST();

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

        attendance.setCheckOutMode(false);

        Attendance saved = attendanceRepository.save(attendance);
        if (saved != null) {
            return "Checked-in Successfully";
        } else {
            return "Error in saving the data";
        }
    }

    // -------------------- CHECK-OUT (FIXED) --------------------
    @Override
    public String checkOut(String userId, double latitude, double longitude,
                           String address, boolean mode) {

        LocalDate today = getTodayIST();

        // ❗ FIX: Get the LATEST check-in record for today
        Attendance attendance =
                attendanceRepository.findTopByUserIdAndDateOrderByCheckInTimeDesc(userId, today);

        if (attendance == null) {
            return "No Check-in today";
        }

        if (attendance.getCheckOutTime() != null) {
            return "Already checked out";
        }

        attendance.setCheckOutMode(mode);
        attendance.setCheckOutTime(getNowIST());
        attendance.setCheckOutLatitude(latitude);
        attendance.setCheckOutLongitude(longitude);
        attendance.setCheckOutAddress(address);

        Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        attendance.setDuration(hours + "h " + minutes + "m");

        Attendance save = attendanceRepository.save(attendance);
        if (save != null) {
            return "Checked-out Successfully";
        } else {
            return "Error in saving the data";
        }
    }

    @Override
    public List<Attendance> getAllAttendance() {
        List<Attendance> list = attendanceRepository.findAll();
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<Attendance> getUserAttendance(String userId) {
        List<Attendance> list = attendanceRepository.findByUserId(userId);

        if (list == null || list.isEmpty()) {
            return List.of();
        }

        List<Attendance> reversed = new ArrayList<>(list);
        reversed.addAll(list);
        Collections.reverse(reversed);
        return reversed;
    }

    @Override
    public List<Attendance> getAttendanceByDateRange(LocalDate start, LocalDate end) {
        List<Attendance> list = attendanceRepository.findByDateBetween(start, end);
        Collections.reverse(list);
        return list;
    }
}
