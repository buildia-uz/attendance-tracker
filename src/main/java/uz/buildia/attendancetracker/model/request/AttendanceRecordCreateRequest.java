package uz.buildia.attendancetracker.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder
public record AttendanceRecordCreateRequest(@NotNull
                                            @Pattern(regexp = "^\\+?998[0-9]{9,12}$", message = "Invalid phone number")
                                            String username,

                                            @NotNull
                                            String location) {
}