package uz.buildia.attendancetracker.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.buildia.attendancetracker.config.SuccessfulAttendanceRecordCreateRequestProvider;
import uz.buildia.attendancetracker.model.entity.AttendanceRecord;
import uz.buildia.attendancetracker.model.entity.Employee;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;
import uz.buildia.attendancetracker.repository.AttendanceRecordRepository;
import uz.buildia.attendancetracker.repository.EmployeeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uz.buildia.attendancetracker.model.constants.AttendanceStatus.ARRIVED;
import static uz.buildia.attendancetracker.model.constants.AttendanceStatus.LEFT;


@ExtendWith(MockitoExtension.class)
class AttendanceRecordServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;

    @InjectMocks
    private AttendanceRecordServiceImpl attendanceRecordService;

    @DisplayName("Test saving attendance record with ARRIVED status")
    @ParameterizedTest(name = "{displayName} - [{index}] - Record: {0}")
    @ArgumentsSource(SuccessfulAttendanceRecordCreateRequestProvider.class)
    void testSaveAttendanceRecordWithArrived_Successful(AttendanceRecordCreateRequest request) {
        Employee employee = Employee.builder()
                .username(request.username())
                .build();

        LocalDateTime todayMidnight = LocalDate.now().atStartOfDay();

        when(employeeRepository.findByUsername(request.username())).thenReturn(Optional.of(employee));
        when(attendanceRecordRepository.findPreviousStatus(request.username(), todayMidnight)).thenReturn(Optional.of(LEFT));

        attendanceRecordService.saveAttendanceRecord(request);

        ArgumentCaptor<AttendanceRecord> captor = ArgumentCaptor.forClass(AttendanceRecord.class);

        verify(employeeRepository, times(1)).findByUsername(request.username());
        verify(attendanceRecordRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(ARRIVED);
    }


    @DisplayName("Test saving attendance record with LEFT status")
    @ParameterizedTest(name = "{displayName} - [{index}] - Record: {0}")
    @ArgumentsSource(SuccessfulAttendanceRecordCreateRequestProvider.class)
    void testSaveAttendanceRecordWithLeft_Successful(AttendanceRecordCreateRequest request) {
        Employee employee = Employee.builder()
                .username(request.username())
                .build();

        LocalDateTime todayMidnight = LocalDate.now().atStartOfDay();

        when(employeeRepository.findByUsername(request.username())).thenReturn(Optional.of(employee));
        when(attendanceRecordRepository.findPreviousStatus(request.username(), todayMidnight)).thenReturn(Optional.of(ARRIVED));

        ArgumentCaptor<AttendanceRecord> captor = ArgumentCaptor.forClass(AttendanceRecord.class);

        attendanceRecordService.saveAttendanceRecord(request);

        verify(employeeRepository, times(1)).findByUsername(request.username());
        verify(attendanceRecordRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(LEFT);
    }

    @DisplayName("Test saving attendance record with invalid username format")
    @ParameterizedTest(name = "{displayName} - [{index}] - Record: {0}")
    @ArgumentsSource(SuccessfulAttendanceRecordCreateRequestProvider.class)
    void testSaveAttendanceRecord_InvalidUsernameFormat(AttendanceRecordCreateRequest request) {
        assertThrows(EntityNotFoundException.class, () -> attendanceRecordService.saveAttendanceRecord(request));
    }
}
