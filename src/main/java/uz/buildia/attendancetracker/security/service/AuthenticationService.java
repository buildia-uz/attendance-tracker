package uz.buildia.attendancetracker.security.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.buildia.attendancetracker.model.request.SignInRequest;
import uz.buildia.attendancetracker.model.request.SignUpRequest;
import uz.buildia.attendancetracker.model.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);
    AuthenticationResponse signIn(SignInRequest request);
    AuthenticationResponse refreshToken(HttpServletRequest request);
}
