package uz.buildia.attendancetracker.security.service;

import uz.buildia.attendancetracker.entity.User;

public interface UserService {
    User getUserByEmail(String email);
    User saveUser(User user);
}
