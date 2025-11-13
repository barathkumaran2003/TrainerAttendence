package com.app.trainerattendence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leave_balances")
public class LeaveBalance {
    @Id
    private String id;

    private String userId;
    // CL, SL, WFH, COMPOFF
    private String leaveType;

    // Use decimal to support half-day (0.5)
    private double balance;
}
