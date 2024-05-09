package uz.buildia.attendancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.buildia.attendancetracker.entity.Token;
import uz.buildia.attendancetracker.entity.User;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE Token e SET e.revoked = true, e.expired = true WHERE e.token = :token")
    void setRevokedAndExpiredByToken(@Param("token") String token);

    @Modifying
    @Transactional
    @Query("UPDATE Token e SET e.revoked = true, e.expired = true WHERE e.user = :user")
    void setAllRevokedAndExpiredByUser(@Param("user") User user);
}
