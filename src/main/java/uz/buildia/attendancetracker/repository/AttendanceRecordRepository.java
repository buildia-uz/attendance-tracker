package uz.buildia.attendancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.buildia.attendancetracker.constants.AttendanceStatus;
import uz.buildia.attendancetracker.entity.AttendanceRecord;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    /**
     * Finds the previous status of attendance for the given employee.
     *
     * @param username        The username of the employee.
     * @param todayMidnight   The date and time representing today's midnight.
     * @return                An Optional containing the previous AttendanceStatus,
     *                        or an empty Optional if no status is found.
     */
    @Query("""
        SELECT r.status FROM AttendanceRecord r
        JOIN Employee e ON r.employee.id = e.id
        WHERE e.username = ?1 AND r.recordCreatedAt >= ?2
        ORDER BY r.recordCreatedAt DESC LIMIT 1
        """)
    Optional<AttendanceStatus> findPreviousStatus(String username, LocalDateTime todayMidnight);

}
