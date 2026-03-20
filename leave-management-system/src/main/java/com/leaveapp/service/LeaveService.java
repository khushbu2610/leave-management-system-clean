package com.leaveapp.service;

import com.leaveapp.model.Employee;
import com.leaveapp.model.Leave;
import com.leaveapp.model.Leave.LeaveStatus;
import com.leaveapp.repository.EmployeeRepository;
import com.leaveapp.repository.LeaveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;

    public LeaveService(EmployeeRepository employeeRepository,
                        LeaveRepository leaveRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
    }

    public Employee addEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Employee with this email already exists: " + employee.getEmail());
        }
        employee.setUsedLeaves(0);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Transactional
    public Leave applyLeave(Long employeeId, Leave leave) {
        Employee employee = getEmployeeById(employeeId);

        long leaveDays = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        int remainingLeaves = employee.getTotalLeaves() - employee.getUsedLeaves();

        if (leaveDays > remainingLeaves) {
            throw new RuntimeException(
                "Insufficient leave balance. Requested: " + leaveDays + ", Available: " + remainingLeaves
            );
        }

        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);
        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave updateLeaveStatus(Long leaveId, LeaveStatus newStatus) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found with id: " + leaveId));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Only PENDING leaves can be updated. Current status: " + leave.getStatus());
        }

        leave.setStatus(newStatus);

        if (newStatus == LeaveStatus.APPROVED) {
            Employee emp = leave.getEmployee();
            long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            emp.setUsedLeaves((int)(emp.getUsedLeaves() + days));
            employeeRepository.save(emp);
        }

        return leaveRepository.save(leave);
    }

    public List<Leave> getLeavesByEmployee(Long employeeId) {
        getEmployeeById(employeeId);
        return leaveRepository.findByEmployeeId(employeeId);
    }

    public List<Leave> getPendingLeaves() {
        return leaveRepository.findByStatus(LeaveStatus.PENDING);
    }

    public String getLeaveBalance(Long employeeId) {
        Employee emp = getEmployeeById(employeeId);
        int remaining = emp.getTotalLeaves() - emp.getUsedLeaves();
        return "Employee: " + emp.getName() +
               " | Total: " + emp.getTotalLeaves() +
               " | Used: " + emp.getUsedLeaves() +
               " | Remaining: " + remaining;
    }
}
