package com.java.centralizedNotificationBackend.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtRequest {

    String username;
    String password;
}
