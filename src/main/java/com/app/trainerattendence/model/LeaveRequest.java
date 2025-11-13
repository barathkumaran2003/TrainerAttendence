package com.app.trainerattendence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leave_requests")
public class LeaveRequest {
    @Id
    private String id;

    private String userId;
    private String userName;        // for quick display
    private String leaveType;       // CL, SL, WFH, COMPOFF

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    // PENDING, APPROVED, REJECTED
    private String status;

    // Half-day support
    private boolean halfDay;        // true if half-day
    private String halfSession;     // "AM" or "PM" (optional when halfDay=false)
}
