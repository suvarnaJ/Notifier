package com.java.centralizedNotificationBackend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePassword {

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

}
