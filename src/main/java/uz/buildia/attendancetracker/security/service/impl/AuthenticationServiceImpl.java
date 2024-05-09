package uz.buildia.attendancetracker.security.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.buildia.attendancetracker.exception.AuthenticationException;
import uz.buildia.attendancetracker.entity.Token;
import uz.buildia.attendancetracker.entity.User;
import uz.buildia.attendancetracker.enums.TokenType;
import uz.buildia.attendancetracker.model.request.SignInRequest;
import uz.buildia.attendancetracker.model.request.SignUpRequest;
import uz.buildia.attendancetracker.model.response.AuthenticationResponse;
import uz.buildia.attendancetracker.repository.TokenRepository;
import uz.buildia.attendancetracker.security.service.AuthenticationService;
import uz.buildia.attendancetracker.security.service.JwtService;
import uz.buildia.attendancetracker.security.service.UserService;

import java.util.Optional;

import static uz.buildia.attendancetracker.constants.ErrorConstants.INVALID_REFRESH_TOKEN;
import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_TOKEN_HEADER_PREFIX;
import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_TOKEN_HEADER_PREFIX_LENGTH;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse signUp(SignUpRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        User savedUser = userService.saveUser(user);

        String jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtService.generateToken(user))
                .build();
    }

    @Transactional
    public AuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.getUserByEmail(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        String refreshToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(AUTH_TOKEN_HEADER_PREFIX))
                .map(header -> header.substring(AUTH_TOKEN_HEADER_PREFIX_LENGTH))
                .orElseThrow(() -> new AuthenticationException(INVALID_REFRESH_TOKEN));

        Pair<String, String> tokens = jwtService.extractUsername(refreshToken)
                .map(userService::getUserByEmail)
                .filter(user -> jwtService.isValidToken(refreshToken, user))
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    return Pair.of(accessToken, jwtService.generateToken(user));
                })
                .orElseThrow(() -> new AuthenticationException(INVALID_REFRESH_TOKEN));

        return AuthenticationResponse.builder()
                .accessToken(tokens.getFirst())
                .refreshToken(tokens.getSecond())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.setAllRevokedAndExpiredByUser(user);
    }
}
