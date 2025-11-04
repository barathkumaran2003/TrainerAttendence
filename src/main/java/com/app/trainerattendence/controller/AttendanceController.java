package com.app.trainerattendence.controller;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Attendance APIs", description = "Endpoints for check-in, check-out, and fetching attendance records")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Check-in", description = "Records user's check-in time and location.")
    @PostMapping("/checkin")
    public Attendance checkIn(@RequestBody Map<String, Object> body) {
        String userId = (String) body.get("userId");
        String department = (String) body.get("department");
        double lat = Double.parseDouble(body.get("latitude").toString());
        double lon = Double.parseDouble(body.get("longitude").toString());
        return attendanceService.checkIn(userId, department, lat, lon);
    }

    @Operation(summary = "Check-out", description = "Records user's check-out time, calculates duration.")
    @PostMapping("/checkout")
    public Attendance checkOut(@RequestBody Map<String, Object> body) {
        String userId = (String) body.get("userId");
        double lat = Double.parseDouble(body.get("latitude").toString());
        double lon = Double.parseDouble(body.get("longitude").toString());
        return attendanceService.checkOut(userId, lat, lon);
    }

    @Operation(summary = "Get all attendance records", description = "Fetches all attendance data.")
    @GetMapping("/all")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @Operation(summary = "Get user's attendance", description = "Fetch attendance records for a specific user.")
    @GetMapping("/user/{userId}")
    public List<Attendance> getUserAttendance(@PathVariable String userId) {
        return attendanceService.getUserAttendance(userId);
    }

    @Operation(summary = "Get attendance by date range", description = "Fetch attendance records between two dates.")
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
