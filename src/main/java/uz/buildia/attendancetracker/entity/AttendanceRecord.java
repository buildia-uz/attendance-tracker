package uz.buildia.attendancetracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.buildia.attendancetracker.constants.AttendanceStatus;

import java.time.LocalDateTime;

/**
 * Represents a record of employee attendance in the system.
 * Each attendance record is uniquely identified by its ID.
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance_record")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The status indicating whether the employee arrived or departed.
     * See {@link AttendanceStatus} for possible values.
     */
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    /**
     * The employee associated with this attendance record.
     * See {@link Employee}.
     */
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    /**
     * The timestamp indicating when the attendance record was made.
     */
    private LocalDateTime recordCreatedAt;

    /**
     * The location where the attendance record was made.
     * This is useful for employers with multiple locations.
     */
    private String location;

}
