package uz.buildia.attendancetracker.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.buildia.attendancetracker.security.service.JwtService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static uz.buildia.attendancetracker.constants.SecurityConstants.TOKEN_EXPIRATION_TIME;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "secret";

    @Override
    public Optional<String> extractUsername(String token) {
        return Optional.ofNullable(extractClaim(token, Claims::getSubject));
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        return extractUsername(token)
                .filter(userDetails.getUsername()::equals)
                .filter(this::isTokenNotExpired)
                .stream()
                .findAny()
                .isPresent();
    }

    private boolean isTokenNotExpired(String token) {
        return extractExpiration(token)
                .filter(dateInFuture -> dateInFuture.after(new Date()))
                .isPresent();
    }

    private Optional<Date> extractExpiration(String token) {
        return Optional.ofNullable(extractClaim(token, Claims::getExpiration));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
