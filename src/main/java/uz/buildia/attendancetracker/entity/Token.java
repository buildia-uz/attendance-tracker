package uz.buildia.attendancetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.buildia.attendancetracker.enums.TokenType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    public TokenType tokenType = TokenType.BEARER;

    @Column(nullable = false)
    public boolean revoked;

    @Column(nullable = false)
    public boolean expired;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    public User user;
}
