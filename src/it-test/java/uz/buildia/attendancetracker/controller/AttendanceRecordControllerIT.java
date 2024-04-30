package uz.buildia.attendancetracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uz.buildia.attendancetracker.BaseIT;
import uz.buildia.attendancetracker.entity.AttendanceRecord;
import uz.buildia.attendancetracker.entity.Employee;
import uz.buildia.attendancetracker.model.constants.AttendanceStatus;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;
import uz.buildia.attendancetracker.repository.AttendanceRecordRepository;
import uz.buildia.attendancetracker.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class AttendanceRecordControllerIT extends BaseIT {

    private static final String API_V1_ATTENDANCE_RECORD = "/api/v1/attendance-record";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @AfterEach
    void tearDown() {
        attendanceRecordRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Save very fresh new attendance, expect status arrived")
    void saveVeryFreshNewAttendance() {
        String username = "+998123456789";
        createAndGetEmployee(username);
        AttendanceRecordCreateRequest request = createAndGetAttendanceRecordCreateRequest(username);

        AttendanceStatus statusBefore = attendanceRecordRepository.findPreviousStatus(username, LocalDate.now().atStartOfDay())
                .orElse(AttendanceStatus.LEFT);
        assertEquals(AttendanceStatus.LEFT, statusBefore);

        client.post()
                .uri(API_V1_ATTENDANCE_RECORD)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> log.info(response.toString()));

        AttendanceStatus statusAfter = attendanceRecordRepository.findPreviousStatus(username, LocalDateTime.now().minusMinutes(1))
                .orElseThrow();
        assertEquals(AttendanceStatus.ARRIVED, statusAfter);
    }

    @Test
    @DisplayName("Empployee left the office, expect status left")
    void employeeLeftTheOffice() {
        String username = "+998123456788";
        Employee employee = createAndGetEmployee(username);
        AttendanceRecordCreateRequest request = createAndGetAttendanceRecordCreateRequest(username);
        createArrivedAttendanceRecord(employee, request.location());

        AttendanceStatus statusBefore = attendanceRecordRepository.findPreviousStatus(username, LocalDate.now().atStartOfDay().minusMinutes(1))
                .orElseThrow();
        assertEquals(AttendanceStatus.ARRIVED, statusBefore);

        client.post()
                .uri(API_V1_ATTENDANCE_RECORD)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> log.info(response.toString()));

        AttendanceStatus statusAfter = attendanceRecordRepository.findPreviousStatus(username, LocalDateTime.now().minusMinutes(1))
                .orElseThrow();
        assertEquals(AttendanceStatus.LEFT, statusAfter);
    }

    @Test
    @DisplayName("Employee does not exist, expect 404 status.")
    void employeeDoesNotExist404() {
        String invalidUsername = "+998123456787";
        AttendanceRecordCreateRequest request = createAndGetAttendanceRecordCreateRequest(invalidUsername);

        client.post()
                .uri(API_V1_ATTENDANCE_RECORD)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(response -> log.info(response.toString()));
    }

    @Test
    @DisplayName("Invalid username, expect 400 status.")
    void invalidUsername400() {
        String invalidUsername = "invalid";
        AttendanceRecordCreateRequest request = createAndGetAttendanceRecordCreateRequest(invalidUsername);

        client.post()
                .uri(API_V1_ATTENDANCE_RECORD)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(response -> log.info(response.toString()));
    }

    private Employee createAndGetEmployee(String username) {
        return employeeRepository.saveAndFlush(
                Employee.builder()
                        .firstName("First")
                        .lastName("Last")
                        .username(username)
                        .build()
        );
    }

    private AttendanceRecordCreateRequest createAndGetAttendanceRecordCreateRequest(String username) {
        return new AttendanceRecordCreateRequest(
                username,
                "location"
        );
    }

    private void createArrivedAttendanceRecord(Employee employee, String location) {
        attendanceRecordRepository.saveAndFlush(AttendanceRecord.builder()
                .employee(employee)
                .location(location)
                .status(AttendanceStatus.ARRIVED)
                .recordCreatedAt(LocalDate.now().atStartOfDay())
                .build()
        );
    }
}
