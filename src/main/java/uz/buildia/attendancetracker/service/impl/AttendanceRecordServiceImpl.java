package uz.buildia.attendancetracker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.buildia.attendancetracker.model.constants.AttendanceStatus;
import uz.buildia.attendancetracker.model.entity.AttendanceRecord;
import uz.buildia.attendancetracker.model.entity.Employee;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;
import uz.buildia.attendancetracker.repository.AttendanceRecordRepository;
import uz.buildia.attendancetracker.repository.EmployeeRepository;
import uz.buildia.attendancetracker.service.AttendanceRecordService;

import static uz.buildia.attendancetracker.model.constants.AttendanceStatus.ARRIVED;
import static uz.buildia.attendancetracker.model.constants.AttendanceStatus.LEFT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public void saveAttendanceRecord(AttendanceRecordCreateRequest request) throws EntityNotFoundException {
        Employee employee = employeeRepository.findByUsername(request.username())
                .orElseThrow(EntityNotFoundException::new);

        LocalDateTime recordCreatedAt = LocalDateTime.now();

        LocalDateTime todayMidnight = LocalDate.now().atStartOfDay();

        AttendanceStatus previousStatus = attendanceRecordRepository
                .findPreviousStatus(request.username(), todayMidnight)
                .orElse(LEFT);

        AttendanceStatus status = switch (previousStatus) {
            case ARRIVED -> LEFT;
            default -> ARRIVED;
        };

        AttendanceRecord saved = attendanceRecordRepository.save(AttendanceRecord.builder()
                .employee(employee)
                .location(request.location())
                .status(status)
                .recordCreatedAt(recordCreatedAt)
                .build());

        log.debug("Saved attendance record {}", saved);
    }
}