package com.java.centralizedNotificationBackend.services;

import com.java.centralizedNotificationBackend.entities.User;
import com.java.centralizedNotificationBackend.entities.UserRole;

import java.util.Set;

public interface UserService {

    //creating user
    public User createUser(User user, Set<UserRole> userRoles) throws Exception;

    //get user by username
    public User getUser(String username);

    //get user by userId
    public User getUserById(Long userId);

    //delete user by id
    public void deleteUser(Long userId);
}
