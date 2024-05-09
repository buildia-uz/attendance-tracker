package uz.buildia.attendancetracker.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface JwtService {

    Optional<String> extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isValidToken(String token, UserDetails userDetails);
}
