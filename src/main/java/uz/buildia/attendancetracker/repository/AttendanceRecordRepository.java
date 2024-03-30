package uz.buildia.attendancetracker.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.buildia.attendancetracker.model.constants.AttendanceStatus;
import uz.buildia.attendancetracker.model.entity.AttendanceRecord;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    @Query("""
            SELECT r.status FROM AttendanceRecord r
            JOIN Employee e ON r.employee.id = e.id
            WHERE e.username = ?1 AND r.recordCreatedAt >= ?2
            ORDER BY r.recordCreatedAt DESC LIMIT 1
            """)
    Optional<AttendanceStatus> findPreviousStatus(String username, LocalDateTime todayMidnight);

}