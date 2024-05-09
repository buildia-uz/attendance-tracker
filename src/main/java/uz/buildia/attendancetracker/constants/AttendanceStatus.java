package uz.buildia.attendancetracker.constants;

import lombok.Getter;

/**
 * This enum is used to track the attendance status of an employee.
 * Primarily, it includes statuses for arrival and departure of the employee,
 * but other statuses related to attendance can be added in the future.
 *
 * <p>
 * Available constants:
 * <ul>
 *     <li>{@link #ARRIVED}</li>
 *     <li>{@link #LEFT}</li>
 * </ul>
 * </p>
 */
@Getter
public enum AttendanceStatus {

    ARRIVED,
    LEFT

}
