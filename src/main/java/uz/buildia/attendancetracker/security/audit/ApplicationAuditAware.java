package uz.buildia.attendancetracker.security.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.buildia.attendancetracker.entity.User;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {

    @NonNull
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .filter(auth -> !(auth instanceof AnonymousAuthenticationToken))
                .map(auth -> (User) auth.getPrincipal())
                .map(User::getId);
    }
}
