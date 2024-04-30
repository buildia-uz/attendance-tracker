package uz.buildia.attendancetracker.config;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;

import java.util.stream.Stream;

public class NullAttendanceRecordCreateRequestProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                AttendanceRecordCreateRequest.builder().username(null).location("test_location").build(),
                AttendanceRecordCreateRequest.builder().username(null).location(null).build(),
                AttendanceRecordCreateRequest.builder().username("+99899123456789").location(null).build()
        ).map(Arguments::of);
    }
}
