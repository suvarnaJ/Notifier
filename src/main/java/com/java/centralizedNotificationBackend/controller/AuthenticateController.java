package com.java.centralizedNotificationBackend.controller;

import com.java.centralizedNotificationBackend.config.JwtUtils;
import com.java.centralizedNotificationBackend.entities.JwtRequest;
import com.java.centralizedNotificationBackend.entities.JwtResponse;
import com.java.centralizedNotificationBackend.entities.User;
import com.java.centralizedNotificationBackend.payload.ErrorResponse;
import com.java.centralizedNotificationBackend.payload.SuccessResponse;
import com.java.centralizedNotificationBackend.services.Impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try{
            authenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
        }catch (Exception e){
            e.printStackTrace();
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,e.getMessage());
        }
        UserDetails userDetails=this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        Claims claims = this.jwtUtils.extractAllClaims(token);
        Date expiration = claims.getExpiration();
        Map<String,String> map = new HashMap<String,String>();
        map.put("token",token);
        map.put("tokenExpired", String.valueOf(expiration));
        return SuccessResponse.successHandler(HttpStatus.OK,false,"Login successfully",map);
    }

    private void authenticate(String username,String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException e){
            throw new Exception("USER DISABLED " +e.getMessage());
        }catch (BadCredentialsException e){
            throw new Exception("Invalid Credentials " +e.getMessage());
        }
    }

    //return the details of login user
    @GetMapping("/current-user")
    public User getCurrentUser(Principal principal){
        return (User) this.userDetailsService.loadUserByUsername(principal.getName());
    }
}

