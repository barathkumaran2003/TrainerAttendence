package com.app.trainerattendence.controller;

import com.app.trainerattendence.model.LeaveRequest;
import com.app.trainerattendence.service.LeaveService;
import com.app.trainerattendence.service.LeaveServiceInterface;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave")
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveServiceInterface leaveService;

    // ---------------------------
    // Leave Requests
    // ---------------------------

    @PostMapping("/apply")
    public LeaveRequest applyLeave(@RequestBody LeaveRequest request) {
        return leaveService.applyLeave(request);
    }

    // Half-day apply (alternative route if you want explicit)
    @PostMapping("/apply/half-day")
    public LeaveRequest applyHalfDay(@RequestBody LeaveRequest request) {
        request.setHalfDay(true);
        // request.setHalfSession("AM" or "PM") must be sent in body
        return leaveService.applyLeave(request);
    }

    @PostMapping("/approve/{requestId}")
    public LeaveRequest approve(@PathVariable String requestId) {
        return leaveService.approveLeave(requestId);
    }

    @PostMapping("/reject/{requestId}")
    public LeaveRequest reject(@PathVariable String requestId) {
        return leaveService.rejectLeave(requestId);
    }

    @GetMapping("/history/{userId}")
    public List<LeaveRequest> history(@PathVariable String userId) {
        return leaveService.getUserLeaveHistory(userId);
    }

    // ---------------------------
    // Calendar UI API
    // ---------------------------

    @GetMapping("/calendar/{userId}")
    public Map<String, Object> calendar(
            @PathVariable String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return leaveService.getCalendar(userId, year, month);
    }

    // ---------------------------
    // Manager Dashboard Summary
    // ---------------------------

    @GetMapping("/manager/summary")
    public List<Map<String, Object>> managerSummary(
            @RequestParam String department,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return leaveService.getManagerSummary(department, year, month);
    }
}
