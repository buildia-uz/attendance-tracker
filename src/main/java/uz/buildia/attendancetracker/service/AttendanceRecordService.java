package uz.buildia.attendancetracker.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;

/**
 * The {@code AttendanceRecordService} interface provides methods for managing attendance records.
 * It defines operations to save attendance records based on the provided request.
 *
 * @see AttendanceRecordCreateRequest
 */
@Service
public interface AttendanceRecordService {

    /**
     * Saves an attendance record based on the provided request.
     *
     * <p>This method retrieves the employee information using the username from the request.
     * It then determines the status of the attendance record (e.g., arrival or departure)
     * based on the previous status of the employee for the current day and saves the attendance record.
     *
     * @param request the request containing information about the attendance record to be saved
     * @throws EntityNotFoundException if the employee information cannot be found
     * @see AttendanceRecordCreateRequest
     */
    void saveAttendanceRecord(AttendanceRecordCreateRequest request) throws EntityNotFoundException;

}
