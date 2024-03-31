package uz.buildia.attendancetracker.config;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;

public class InvalidUsernameAttendanceRecordCreateRequestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                AttendanceRecordCreateRequest.builder().username("invalid_username").location("Tashkent").build(),
                AttendanceRecordCreateRequest.builder().username("+123456789123").location("Tashkent").build(),
                AttendanceRecordCreateRequest.builder().username("999123456789").location("Tashkent").build(),
                AttendanceRecordCreateRequest.builder().username("+999123456789").location("Almaty").build(),
                AttendanceRecordCreateRequest.builder().username("+99812345678").location("Almaty").build(),
                AttendanceRecordCreateRequest.builder().username("99812345678").location("Bishkek").build()
        ).map(Arguments::of);
    }
}
