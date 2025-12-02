package com.app.trainerattendence.service;

import java.time.LocalDate;
import java.util.List;

import com.app.trainerattendence.model.Attendance;

public interface AttendanceServiceInterface {

	String checkIn(String userId, String userName, String department, double latitude, double longitude,
			String address, boolean mode);

	Attendance checkOut(String userId, double latitude, double longitude, String address, boolean mode);

	List<Attendance> getAllAttendance();

	List<Attendance> getUserAttendance(String userId);

	List<Attendance> getAttendanceByDateRange(LocalDate start, LocalDate end);

}
