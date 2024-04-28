package uz.buildia.attendancetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uz.buildia.attendancetracker.config.AttendanceControllerAdvice;
import uz.buildia.attendancetracker.config.InvalidUsernameAttendanceRecordCreateRequestProvider;
import uz.buildia.attendancetracker.config.NullAttendanceRecordCreateRequestProvider;
import uz.buildia.attendancetracker.config.SuccessfulAttendanceRecordCreateRequestProvider;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;
import uz.buildia.attendancetracker.service.AttendanceRecordService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AttendanceRecordControllerTest {

    @Mock
    private AttendanceRecordService attendanceRecordService;

    @InjectMocks
    private AttendanceRecordController attendanceRecordController;

    @InjectMocks
    private AttendanceControllerAdvice attendanceControllerAdvice;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceRecordController)
                .setControllerAdvice(attendanceControllerAdvice)
                .build();
    }

    @DisplayName("Successful attendance record saving")
    @ParameterizedTest(name = "{displayName} - [{index}] - Request: {0}")
    @ArgumentsSource(SuccessfulAttendanceRecordCreateRequestProvider.class)
    void testSaveAttendanceRecord_Successful(AttendanceRecordCreateRequest request) throws Exception {
        String jsonContent = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/attendance-record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Failed attendance record saving due to null values")
    @ParameterizedTest(name = "{displayName} - [{index}] - Request: {0}")
    @ArgumentsSource(NullAttendanceRecordCreateRequestProvider.class)
    void testSaveAttendanceRecord_NullUsername(AttendanceRecordCreateRequest request) throws Exception {
        mockMvc.perform(post("/api/v1/attendance-record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Failed attendance record saving due to invalid username")
    @ParameterizedTest(name = "{displayName} - [{index}] - Request: {0}")
    @ArgumentsSource(InvalidUsernameAttendanceRecordCreateRequestProvider.class)
    void testSaveAttendanceRecord_InvalidUsernameFormat(AttendanceRecordCreateRequest request) throws Exception {
        mockMvc.perform(post("/api/v1/attendance-record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Check BadRequest status due to an invalid username format
    }
}
