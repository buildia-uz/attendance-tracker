package uz.buildia.attendancetracker.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.buildia.attendancetracker.enums.UserRole;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private UserRole role;
}
