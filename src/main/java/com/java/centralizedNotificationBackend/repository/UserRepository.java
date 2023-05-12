package com.java.centralizedNotificationBackend.repository;

import com.java.centralizedNotificationBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByEmail(String username);

    public User findByResetPasswordToken(String token);
}
