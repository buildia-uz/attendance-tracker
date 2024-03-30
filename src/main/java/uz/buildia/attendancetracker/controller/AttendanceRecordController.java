package uz.buildia.attendancetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.buildia.attendancetracker.model.entity.AttendanceRecord;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;
import uz.buildia.attendancetracker.service.AttendanceRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance-record")
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    @PostMapping
    public ResponseEntity<AttendanceRecord> saveAttendanceRecord(@RequestBody AttendanceRecordCreateRequest request) {
        attendanceRecordService.saveAttendanceRecord(request);
        return ResponseEntity.ok().build();
    }

}