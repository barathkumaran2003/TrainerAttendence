package com.app.trainerattendence.service;

import com.app.trainerattendence.model.Attendance;
import com.app.trainerattendence.model.LeaveBalance;
import com.app.trainerattendence.model.LeaveRequest;
import com.app.trainerattendence.model.User;
import com.app.trainerattendence.repository.AttendanceRepository;
import com.app.trainerattendence.repository.LeaveBalanceRepository;
import com.app.trainerattendence.repository.LeaveRequestRepository;
import com.app.trainerattendence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveService implements LeaveServiceInterface{

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    private LocalDate todayIST() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDate();
    }

    private LocalDateTime nowIST() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }

    // -------------------------------------------------------
    // Leave Requests Basic Ops
    // -------------------------------------------------------
    @Override
    public LeaveRequest applyLeave(LeaveRequest request) {
        request.setStatus("PENDING");
        return leaveRequestRepository.save(request);
    }
    
    @Override
    public LeaveRequest approveLeave(String requestId) {
        LeaveRequest req = leaveRequestRepository.findById(requestId).orElse(null);
        if (req == null) return null;

        req.setStatus("APPROVED");
        leaveRequestRepository.save(req);

        markLeaveIntoAttendance(req);
        deductLeaveBalance(req);

        return req;
    }

    @Override
    public LeaveRequest rejectLeave(String requestId) {
        LeaveRequest req = leaveRequestRepository.findById(requestId).orElse(null);
        if (req == null) return null;

        req.setStatus("REJECTED");
        return leaveRequestRepository.save(req);
    }

    @Override
    public List<LeaveRequest> getUserLeaveHistory(String userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    // -------------------------------------------------------
    // Add Leave into Attendance Records
    // -------------------------------------------------------

    private void markLeaveIntoAttendance(LeaveRequest req) {

        LocalDate d = req.getStartDate();

        // ❗ Half Day Leave
        if (req.isHalfDay()) {
            Attendance a = new Attendance();

            a.setUserId(req.getUserId());
            a.setUserName(req.getUserName());
            a.setDepartment("N/A");
            a.setDate(req.getStartDate());

            // Mark as leave (not present)
            a.setCheckInMode(false);
            a.setCheckOutMode(false);

            String label = "HALF DAY - " + req.getLeaveType();
            if (req.getHalfSession() != null) {
                label += " (" + req.getHalfSession() + ")";
            }

            a.setDuration(label);
            attendanceRepository.save(a);
            return;
        }

        // ❗ Full Day Leave/WFH/Compoff
        while (!d.isAfter(req.getEndDate())) {

            Attendance a = new Attendance();
            a.setUserId(req.getUserId());
            a.setUserName(req.getUserName());
            a.setDepartment("N/A");
            a.setDate(d);

            switch (req.getLeaveType().toUpperCase()) {

                case "WFH":
                    a.setCheckInMode(true);  // WFH treated as work
                    a.setCheckOutMode(true);
                    a.setDuration("FULL DAY - WFH");
                    break;

                case "COMPOFF":
                    a.setCheckInMode(false);
                    a.setCheckOutMode(false);
                    a.setDuration("COMPOFF LEAVE");
                    break;

                default:
                    a.setCheckInMode(false);
                    a.setCheckOutMode(false);
                    a.setDuration("FULL DAY - " + req.getLeaveType());
                    break;
            }

            attendanceRepository.save(a);
            d = d.plusDays(1);
        }
    }

    // -------------------------------------------------------
    // Deduct Leave Balance
    // -------------------------------------------------------

    private void deductLeaveBalance(LeaveRequest req) {

        double units = req.isHalfDay()
                ? 0.5
                : (req.getEndDate().toEpochDay() - req.getStartDate().toEpochDay() + 1);

        LeaveBalance bal =
                leaveBalanceRepository.findByUserIdAndLeaveType(req.getUserId(), req.getLeaveType());

        if (bal == null) return;

        bal.setBalance(Math.max(bal.getBalance() - units, 0));
        leaveBalanceRepository.save(bal);
    }

    // -------------------------------------------------------
    // Calendar Month View
    // -------------------------------------------------------
    
    @Override
    public Map<String, Object> getCalendar(String userId, int year, int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> attendance = attendanceRepository.findByUserId(userId).stream()
                .filter(a -> a.getDate() != null &&
                        !a.getDate().isBefore(start) &&
                        !a.getDate().isAfter(end))
                .collect(Collectors.toList());

        Map<LocalDate, List<Attendance>> byDate =
                attendance.stream().collect(Collectors.groupingBy(Attendance::getDate));

        List<Map<String, Object>> days = new ArrayList<>();
        LocalDate cur = start;

        while (!cur.isAfter(end)) {

            Map<String, Object> day = new LinkedHashMap<>();
            day.put("date", cur.toString());

            // WEEKEND
            if (cur.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    cur.getDayOfWeek() == DayOfWeek.SUNDAY) {

                day.put("status", "NON_WORKING");
                day.put("details", "Weekend");
                days.add(day);

                cur = cur.plusDays(1);
                continue;
            }

            List<Attendance> logs = byDate.getOrDefault(cur, Collections.emptyList());

            if (logs.isEmpty()) {
                day.put("status", "ABSENT");
                day.put("details", "No attendance record");
            } else {
                Attendance a = logs.get(0);

                String status = "UNKNOWN";

                if (a.getDuration() != null && a.getDuration().contains("HALF DAY")) {
                    status = "HALF_DAY_LEAVE";

                } else if (a.getDuration() != null && a.getDuration().contains("LEAVE")) {
                    status = "LEAVE";

                } else if (a.isCheckInMode() && a.isCheckOutMode()) {
                    status = "PRESENT_WFH";

                } else if (a.getCheckInTime() != null || a.getCheckOutTime() != null) {
                    status = "PRESENT_WFO";
                }

                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("checkIn", a.getCheckInTime());
                meta.put("checkOut", a.getCheckOutTime());
                meta.put("duration", a.getDuration());
                meta.put("wfh", a.isCheckInMode());

                day.put("status", status);
                day.put("details", meta);
            }

            days.add(day);
            cur = cur.plusDays(1);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userId", userId);
        result.put("year", year);
        result.put("month", month);
        result.put("days", days);

        return result;
    }

    // -------------------------------------------------------
    // Manager Summary (Department Monthly Report)
    // -------------------------------------------------------

    @Override
    public List<Map<String, Object>> getManagerSummary(String department, int year, int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<User> users = userRepository.findByDepartmentIgnoreCase(department);

        List<Map<String, Object>> summaries = new ArrayList<>();

        for (User u : users) {

            List<Attendance> records =
                    attendanceRepository.findByUserId(u.getUserId()).stream()
                            .filter(a -> a.getDate() != null &&
                                    !a.getDate().isBefore(start) &&
                                    !a.getDate().isAfter(end))
                            .collect(Collectors.toList());

            long wfhDays = records.stream().filter(a -> a.isCheckInMode()).map(Attendance::getDate).distinct().count();

            long leaveDays = records.stream()
                    .filter(a -> a.getDuration() != null && a.getDuration().contains("LEAVE"))
                    .map(Attendance::getDate).distinct().count();

            long halfLeaveDays = records.stream()
                    .filter(a -> a.getDuration() != null && a.getDuration().contains("HALF"))
                    .map(Attendance::getDate).distinct().count();

            long officeDays = records.stream()
                    .filter(a ->
                            !a.isCheckInMode() &&
                                    (a.getCheckInTime() != null || a.getCheckOutTime() != null))
                    .map(Attendance::getDate).distinct().count();

            long totalMinutes = records.stream()
                    .filter(a -> a.getCheckInTime() != null && a.getCheckOutTime() != null)
                    .mapToLong(a -> Duration.between(a.getCheckInTime(), a.getCheckOutTime()).toMinutes())
                    .sum();

            long th = totalMinutes / 60;
            long tm = totalMinutes % 60;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("userId", u.getUserId());
            row.put("name", u.getName());
            row.put("department", department);

            row.put("presentOfficeDays", officeDays);
            row.put("presentWfhDays", wfhDays);
            row.put("leaveDays", leaveDays);
            row.put("halfLeaveDays", halfLeaveDays);
            row.put("totalHoursWorked", th + "h " + tm + "m");

            summaries.add(row);
        }

        return summaries;
    }
}
