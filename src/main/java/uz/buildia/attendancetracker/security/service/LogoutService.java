package uz.buildia.attendancetracker.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.buildia.attendancetracker.repository.TokenRepository;

import java.util.Optional;

import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_TOKEN_HEADER_PREFIX;
import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_TOKEN_HEADER_PREFIX_LENGTH;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(AUTH_TOKEN_HEADER_PREFIX))
                .map(header -> header.substring(AUTH_TOKEN_HEADER_PREFIX_LENGTH))
                .ifPresent(tokenRepository::setRevokedAndExpiredByToken);
    }
}
