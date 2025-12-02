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
    private String userName;
    private String department;

    private LocalDate date;

    private boolean checkInMode;
    private boolean checkOutMode;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private double checkInLatitude;
    private double checkInLongitude;
    private String checkInAddress;

    private double checkOutLatitude;
    private double checkOutLongitude;
    private String checkOutAddress;

    private String duration;
}
