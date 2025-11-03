package com.app.trainerattendence.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/checkin")
    public Attendance checkIn(@RequestBody Map<String, Object> body) {
        String userId = (String) body.get("userId");
        String department = (String) body.get("department");
        double lat = Double.parseDouble(body.get("latitude").toString());
        double lon = Double.parseDouble(body.get("longitude").toString());
        return attendanceService.checkIn(userId, department, lat, lon);
    }

    @PostMapping("/checkout")
    public Attendance checkOut(@RequestBody Map<String, Object> body) {
        String userId = (String) body.get("userId");
        double lat = Double.parseDouble(body.get("latitude").toString());
        double lon = Double.parseDouble(body.get("longitude").toString());
        return attendanceService.checkOut(userId, lat, lon);
    }

    @GetMapping("/all")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/user/{userId}")
    public List<Attendance> getUserAttendance(@PathVariable String userId) {
        return attendanceService.getUserAttendance(userId);
    }

    @GetMapping("/range")
    public List<Attendance> getAttendanceByRange(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return attendanceService.getAttendanceByDateRange(start, end);
    }
}
