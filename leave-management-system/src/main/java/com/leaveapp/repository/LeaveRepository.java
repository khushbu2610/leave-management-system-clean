package com.leaveapp.repository;

import com.leaveapp.model.Leave;
import com.leaveapp.model.Leave.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployeeId(Long employeeId);
    List<Leave> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);
    List<Leave> findByStatus(LeaveStatus status);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.employee.id = :employeeId AND l.status = 'APPROVED'")
    int countApprovedLeavesByEmployee(@Param("employeeId") Long employeeId);
}
