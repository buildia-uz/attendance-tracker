package uz.buildia.attendancetracker.exception;

public class EmployeeException extends RuntimeException {
    public EmployeeException() {
    }

    public EmployeeException(String message) {
        super(message);
    }
}