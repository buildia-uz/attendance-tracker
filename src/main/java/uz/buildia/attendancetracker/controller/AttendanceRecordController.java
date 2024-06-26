package uz.buildia.attendancetracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;
import uz.buildia.attendancetracker.service.AttendanceRecordService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance-record")
@CrossOrigin(origins = "http://localhost")
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    @PostMapping
    public ResponseEntity<Void> saveAttendanceRecord(@Valid @RequestBody AttendanceRecordCreateRequest request) {
        attendanceRecordService.saveAttendanceRecord(request);
        return ResponseEntity.ok().build();
    }
}
