package uz.buildia.attendancetracker.config;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import uz.buildia.attendancetracker.model.request.AttendanceRecordCreateRequest;

public class SuccessfulAttendanceRecordCreateRequestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                AttendanceRecordCreateRequest.builder().username("+998712548845").location("Tashkent").build(),
                AttendanceRecordCreateRequest.builder().username("998712548845").location("Tashkent").build()
        ).map(Arguments::of);
    }
}