package uz.buildia.attendancetracker.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.buildia.attendancetracker.repository.TokenRepository;
import uz.buildia.attendancetracker.security.service.JwtService;

import java.io.IOException;
import java.util.Optional;

import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_API_ENTRYPOINT;
import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_TOKEN_HEADER_PREFIX;
import static uz.buildia.attendancetracker.constants.SecurityConstants.AUTH_TOKEN_HEADER_PREFIX_LENGTH;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().startsWith(AUTH_API_ENTRYPOINT)) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<String> jwtToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(AUTH_TOKEN_HEADER_PREFIX))
                .map(header -> header.substring(AUTH_TOKEN_HEADER_PREFIX_LENGTH));
        if (jwtToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtService.extractUsername(jwtToken.get())
                .ifPresent(userEmail -> processAuthentication(request, jwtToken.get(), userEmail));
        filterChain.doFilter(request, response);
    }

    private void processAuthentication(HttpServletRequest request, String jwtToken, @NonNull String userEmail) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            boolean isTokenValid = tokenRepository.findByToken(jwtToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isValidToken(jwtToken, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                tokenRepository.setRevokedAndExpiredByToken(jwtToken);
            }
        }
    }
}
