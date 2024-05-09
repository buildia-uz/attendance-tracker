package uz.buildia.attendancetracker.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.buildia.attendancetracker.model.request.SignInRequest;
import uz.buildia.attendancetracker.model.request.SignUpRequest;
import uz.buildia.attendancetracker.model.response.AuthenticationResponse;
import uz.buildia.attendancetracker.security.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(service.signUp(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(service.signIn(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

}
