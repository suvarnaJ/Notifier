package com.java.centralizedNotificationBackend.services;

import com.java.centralizedNotificationBackend.entities.User;
import com.java.centralizedNotificationBackend.exceptions.UserNotFoundException;
import com.java.centralizedNotificationBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

}
