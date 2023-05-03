package com.java.centralizedNotificationBackend.controller;

import com.java.centralizedNotificationBackend.config.JwtUtils;
import com.java.centralizedNotificationBackend.entities.Role;
import com.java.centralizedNotificationBackend.entities.User;
import com.java.centralizedNotificationBackend.entities.UserRole;
import com.java.centralizedNotificationBackend.entities.UserTemplates;
import com.java.centralizedNotificationBackend.helper.FileUploadHelper;
import com.java.centralizedNotificationBackend.payload.ErrorResponse;
import com.java.centralizedNotificationBackend.payload.SuccessResponse;
import com.java.centralizedNotificationBackend.repository.UserTemplateRepository;
import com.java.centralizedNotificationBackend.services.UserService;
import com.java.centralizedNotificationBackend.services.UserTemplateService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
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

    @Autowired
    FileUploadHelper fileUploadHelper;

    @Autowired
    private UserTemplateService userTemplateService;

    @Autowired
    private UserTemplateRepository userTemplateRepository;

    @Autowired
    private JwtUtils jwtUtil;

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

    @PostMapping("/{userId}/upload-file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,@PathVariable("userId") Long userId){
        try {
            if (file.isEmpty()) {
                return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,"Request must contain file");
            }
            if (!file.getContentType().equals("text/html")) {
                return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,"Only HTML content are allowed");
            }
            boolean f = fileUploadHelper.uploadFile(file);
            if(f){
                UserTemplates userTemplates = new UserTemplates();
                UserTemplates userTemplateByUserTemplate = userTemplateRepository.getUserTemplateByUserTemplate(file.getOriginalFilename());
                if(userTemplateByUserTemplate==null){
                    userTemplates.setUserTemplate(file.getOriginalFilename());
                    User user = this.userService.getUserById(userId);
                    userTemplates.setUser(user);
                    this.userTemplateService.createUserTemplate(userTemplates);
                    return SuccessResponse.successHandler(HttpStatus.OK,false,"File uploaded successfully",null);
                }else{
                    userTemplateService.updateUserTemplate(userTemplateByUserTemplate.getId(), userTemplateByUserTemplate);
                    return SuccessResponse.successHandler(HttpStatus.OK,false,"File uploaded successfully",null);
                }
            }
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,ex.getMessage());
        }
        return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,"Something went wrong ! Try again");
    }


    @GetMapping("/{userId}/get-all-templates")
    public ResponseEntity<?> getUserTemplatesByUser(@PathVariable("userId") Long userId,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        try{
            Pageable pageable = PageRequest.of(page,size);
            Page<UserTemplates> userTemplatesList = this.userTemplateRepository.findUserTemplatesByUser(userId,pageable);
            return SuccessResponse.successHandler(HttpStatus.OK,false,"Templates fetched successfully",userTemplatesList);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,ex.getMessage());
        }
    }

    @GetMapping("/{userId}/get-single-template/{templateId}")
    public ResponseEntity<?> getUserTemplateByTemplateId(@PathVariable("userId") Long userId,@PathVariable("templateId") Long templateId){
        try{
            UserTemplates userTemplate = this.userTemplateService.getUserTemplateById(templateId);
            return SuccessResponse.successHandler(HttpStatus.OK,false,"Templates fetched successfully",userTemplate);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}/delete-single-template/{templateId}")
    public ResponseEntity<?> deleteUserTemplateByTemplateId(@PathVariable("userId") Long userId,@PathVariable("templateId") Long templateId){
        try{
            UserTemplates userTemplateById = this.userTemplateService.getUserTemplateById(templateId);
            if(userId.equals(userTemplateById.getUser().getId())){
                this.userTemplateService.deleteUserTemplate(templateId);
                return SuccessResponse.successHandler(HttpStatus.OK,false,"Templates deleted successfully",null);
            }else{
                return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,"User not found");
            }
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}/delete-all-templates")
    public ResponseEntity<?> deleteAllUserTemplates(@PathVariable("userId") Long userId){
        try{
            this.userTemplateService.deleteAllUserTemplate();
            return SuccessResponse.successHandler(HttpStatus.OK,false,"Templates deleted successfully",null);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,ex.getMessage());
        }
    }

    @GetMapping("/{userId}/export-file/{fileName}")
    public ResponseEntity<?> exportFile(@PathVariable("fileName") String fileName){
        try{
            String fileContent = fileUploadHelper.load(fileName);
            return SuccessResponse.successHandler(HttpStatus.OK,false,"Templates fetched successfully",fileContent);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.INTERNAL_SERVER_ERROR,true,ex.getMessage());
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

    //delete the user by id
    @GetMapping("/expireTokenStatus")
    public ResponseEntity<?> expireTokenStatus(@RequestParam("token") String token) {
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return SuccessResponse.successHandler(HttpStatus.OK, false, "Claimed fetched successfully", expiration);
        }catch (ExpiredJwtException ex){
            return ErrorResponse.errorHandler(HttpStatus.UNAUTHORIZED,true,ex.getMessage());
        }
    }
}
