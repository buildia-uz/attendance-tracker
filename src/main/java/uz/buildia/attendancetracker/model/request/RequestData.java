package uz.buildia.attendancetracker.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestData {
    private AttendanceRecordCreateRequest data;
}
