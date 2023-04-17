package com.java.centralizedNotificationBackend.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userRoleId;

    //user
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne
    private  Role role;
}
