package com.leaveapp.controller;

import com.leaveapp.model.Employee;
import com.leaveapp.model.Leave;
import com.leaveapp.model.Leave.LeaveStatus;
import com.leaveapp.service.LeaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.addEmployee(employee));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(leaveService.getAllEmployees());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(leaveService.getEmployeeById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/employees/{id}/balance")
    public ResponseEntity<String> getLeaveBalance(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(leaveService.getLeaveBalance(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/employees/{employeeId}/leaves")
    public ResponseEntity<Leave> applyLeave(@PathVariable Long employeeId,
                                             @RequestBody Leave leave) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.applyLeave(employeeId, leave));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employees/{employeeId}/leaves")
    public ResponseEntity<List<Leave>> getEmployeeLeaves(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/leaves/pending")
    public ResponseEntity<List<Leave>> getPendingLeaves() {
        return ResponseEntity.ok(leaveService.getPendingLeaves());
    }

    @PutMapping("/leaves/{leaveId}/status")
    public ResponseEntity<Leave> updateLeaveStatus(@PathVariable Long leaveId,
                                                    @RequestBody StatusRequest statusRequest) {
        try {
            return ResponseEntity.ok(leaveService.updateLeaveStatus(
                leaveId, LeaveStatus.valueOf(statusRequest.getStatus())
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    static class StatusRequest {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
