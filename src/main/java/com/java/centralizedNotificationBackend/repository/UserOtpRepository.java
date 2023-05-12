package com.java.centralizedNotificationBackend.repository;

import com.java.centralizedNotificationBackend.entities.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp,Long> {

    public UserOtp getOneTimePasswordByOneTimePassword(String oneTimePassword);
}
