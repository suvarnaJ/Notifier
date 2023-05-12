package com.java.centralizedNotificationBackend.services;

import com.java.centralizedNotificationBackend.entities.UserOtp;

import java.util.List;

public interface UserOtpService {

    //creating userOtp
    public UserOtp createUserOtp(UserOtp userOtp);

    //get userOtp by userOtpId
    public UserOtp getUserOtpById(Long userOtpId);

    //get all userOtp
    public List<UserOtp> getAllUserOtp();

    //delete userOtp by id
    public void deleteUserOtp(Long userOtpId);

    //delete userOtp
    public void deleteAllUserOtp();

    //update userOtp by id
    public UserOtp updateUserOtp(Long id,UserOtp userOtp);
}
