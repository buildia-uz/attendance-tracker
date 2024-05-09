package uz.buildia.attendancetracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Represents an employee in the system.
 * Each employee is uniquely identified by their username.
 * Username is basically user's phone number.
 * </p>
 *
 * <p>
 * Employees data should be moved to separate service in the future, when other systems will be implemented.
 * </p>
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the employee, which is typically the phone number.
     * This username is used for user identification.
     */
    private String username;

    private String firstName;
    private String lastName;

}
