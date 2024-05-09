package uz.buildia.attendancetracker.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.buildia.attendancetracker.entity.User;
import uz.buildia.attendancetracker.repository.UserRepository;
import uz.buildia.attendancetracker.security.service.UserService;

import static uz.buildia.attendancetracker.constants.ErrorConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.formatted(email)));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
