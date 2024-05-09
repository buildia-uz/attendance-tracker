package uz.buildia.attendancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.buildia.attendancetracker.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
