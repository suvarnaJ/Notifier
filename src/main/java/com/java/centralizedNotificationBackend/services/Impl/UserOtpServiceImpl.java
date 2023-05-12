package com.java.centralizedNotificationBackend.services.Impl;

import com.java.centralizedNotificationBackend.entities.UserOtp;
import com.java.centralizedNotificationBackend.repository.UserOtpRepository;
import com.java.centralizedNotificationBackend.services.UserOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOtpServiceImpl implements UserOtpService {

    @Autowired
    private UserOtpRepository userOtpRepository;

    @Override
    public UserOtp createUserOtp(UserOtp userOtp) {
        return userOtpRepository.save(userOtp);
    }

    @Override
    public UserOtp getUserOtpById(Long userOtpId) {
        return userOtpRepository.findById(userOtpId).get();
    }

    @Override
    public List<UserOtp> getAllUserOtp() {
        return userOtpRepository.findAll();
    }

    @Override
    public void deleteUserOtp(Long userOtpId) {
        userOtpRepository.deleteById(userOtpId);
    }

    @Override
    public void deleteAllUserOtp() {
        userOtpRepository.deleteAll();
    }

    @Override
    public UserOtp updateUserOtp(Long id, UserOtp userOtp) {
        UserOtp userOtp1 = userOtpRepository.findById(id).get();
        userOtp1.setUser(userOtp.getUser());
        userOtp1.setOneTimePassword(userOtp.getOneTimePassword());
        userOtp1.setOtpRequestedTime(userOtp.getOtpRequestedTime());
        return userOtpRepository.save(userOtp1);
    }
}
