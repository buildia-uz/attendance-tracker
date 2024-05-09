package uz.buildia.attendancetracker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static uz.buildia.attendancetracker.enums.Permission.MANAGER_CREATE;
import static uz.buildia.attendancetracker.enums.Permission.MANAGER_DELETE;
import static uz.buildia.attendancetracker.enums.Permission.MANAGER_READ;
import static uz.buildia.attendancetracker.enums.Permission.MANAGER_UPDATE;
import static uz.buildia.attendancetracker.enums.Permission.USER_READ;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER(Set.of(USER_READ)),
    ADMIN(Set.of(Permission.values())),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
