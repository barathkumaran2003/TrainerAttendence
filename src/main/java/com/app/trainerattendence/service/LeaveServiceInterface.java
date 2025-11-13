package com.app.trainerattendence.service;

import java.util.List;
import java.util.Map;

import com.app.trainerattendence.model.LeaveRequest;

public interface LeaveServiceInterface {

	LeaveRequest applyLeave(LeaveRequest request);

	LeaveRequest approveLeave(String requestId);

	LeaveRequest rejectLeave(String requestId);

	List<LeaveRequest> getUserLeaveHistory(String userId);

	Map<String, Object> getCalendar(String userId, int year, int month);

	List<Map<String, Object>> getManagerSummary(String department, int year, int month);

}
