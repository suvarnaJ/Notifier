package com.java.centralizedNotificationBackend.entities;

import lombok.*;

import java.util.Date;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {

    String token;
    Date tokenExpiry;
}
