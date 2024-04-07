package uz.buildia.attendancetracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.buildia.attendancetracker.model.request.RequestData;
import uz.buildia.attendancetracker.service.AttendanceRecordService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance-record")
@CrossOrigin(origins = "http://localhost")
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    @PostMapping
    public ResponseEntity<Void> saveAttendanceRecord(@Valid RequestData request) {
        attendanceRecordService.saveAttendanceRecord(request.getData());
        return ResponseEntity.ok().build();
    }

}