package uz.buildia.attendancetracker.model.request;

public record AttendanceRecordCreateRequest(String username,
                                            String location,
                                            Byte timezone) {
}