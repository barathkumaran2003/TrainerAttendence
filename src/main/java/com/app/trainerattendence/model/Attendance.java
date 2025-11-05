package com.app.trainerattendence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "attendance")
public class Attendance {

    @Id
    private String id;

    private String userId;
    private String userName; // ✅ Display Name
    private String department;

    private LocalDate date;

    private boolean mode;
    
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private double checkInLatitude;
    private double checkInLongitude;
    private String checkInAddress; // ✅ NEW FIELD

    private double checkOutLatitude;
    private double checkOutLongitude;
    private String checkOutAddress; // ✅ NEW FIELD

    private String duration; // e.g., "2h 15m"
}
