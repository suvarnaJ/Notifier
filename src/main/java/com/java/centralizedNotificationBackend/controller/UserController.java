package com.java.centralizedNotificationBackend.controller;

import com.java.centralizedNotificationBackend.entities.Role;
import com.java.centralizedNotificationBackend.entities.User;
import com.java.centralizedNotificationBackend.entities.UserRole;
import com.java.centralizedNotificationBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //creating user
    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {

        ResponseEntity<?> response;

        try {
            user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
            Set<UserRole> roles = new HashSet<>();

            Role role = new Role();
            role.setRoleId(45L);
            role.setRoleName("NORMAL");

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);

            roles.add(userRole);

            User user1 = this.userService.createUser(user, roles);
            response = ResponseEntity.status(HttpStatus.OK).body(user1);
            return response;
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            return response;
        }
    }

    //get user by username
    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
    }

    //delete the user by id
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userService.deleteUser(userId);
    }
}
