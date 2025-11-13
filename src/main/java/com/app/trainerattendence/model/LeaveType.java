package com.app.trainerattendence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leave_types")
public class LeaveType {
    @Id
    private String id;

    // Allowed values: CL, SL, WFH, COMPOFF
    private String name;

    // Total yearly limit (can be 0 for COMPOFF if you only credit manually)
    private int yearlyLimit;
}
