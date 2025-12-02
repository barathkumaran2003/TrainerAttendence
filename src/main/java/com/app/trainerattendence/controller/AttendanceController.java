package com.app.trainerattendence.controller;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.service.AttendanceService;
import com.app.trainerattendence.service.AttendanceServiceInterface;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceServiceInterface attendanceService;

    @PostMapping("/checkin")
    public String checkIn(@RequestBody Map<String, Object> body) {
        return attendanceService.checkIn(
                (String) body.get("userId"),
                (String) body.get("name"),
                (String) body.get("department"),
                Double.parseDouble(body.get("checkInLatitude").toString()),
                Double.parseDouble(body.get("checkInLongitude").toString()),
                (String) body.get("checkInAddress"),
                (boolean) body.get("checkInMode")
        );
    }

    @PostMapping("/checkout")
    public Attendance checkOut(@RequestBody Map<String, Object> body) {
        return attendanceService.checkOut(
                (String) body.get("userId"),
                Double.parseDouble(body.get("checkOutLatitude").toString()),
                Double.parseDouble(body.get("checkOutLongitude").toString()),
                (String) body.get("checkOutAddress"),
                (boolean) body.get("checkOutMode")
        );
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
        return attendanceService.getAttendanceByDateRange(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }
    
    
}
