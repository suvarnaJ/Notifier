package com.java.centralizedNotificationBackend.controller;

import com.java.centralizedNotificationBackend.config.JwtUtils;
import com.java.centralizedNotificationBackend.entities.*;
import com.java.centralizedNotificationBackend.exceptions.UserNotFoundException;
import com.java.centralizedNotificationBackend.helper.FileUploadHelper;
import com.java.centralizedNotificationBackend.payload.ErrorResponse;
import com.java.centralizedNotificationBackend.payload.SuccessResponse;
import com.java.centralizedNotificationBackend.repository.UserOtpRepository;
import com.java.centralizedNotificationBackend.repository.UserRepository;
import com.java.centralizedNotificationBackend.repository.UserTemplateRepository;
import com.java.centralizedNotificationBackend.services.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.*;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOtpService userOtpService;

    @Autowired
    private UserOtpRepository userOtpRepository;

    @Autowired
    private ResetPasswordService resetPasswordService;

    Random random=new Random();

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;   // 5 minutes


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
        }catch (UserNotFoundException ex){
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

    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userService.deleteUser(userId);
    }

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

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestParam("email") String email) {
        try {
            if(email==""){
                return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Field can't be empty");
            }else{
                Map<String,Object> map = new HashMap<String,Object>();
                User user = userRepository.findByEmail(email);
                String token = RandomString.make(30);
                if(user==null){
                    return ErrorResponse.errorHandler(HttpStatus.NOT_FOUND,true,"Email not found");
                }
                int otp = random.nextInt(999999);
                String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
                String message = ""
                        + "<div style='background-color:lightgrey;padding:20px'>"
                        + "<div style='background-color:white;padding:30px'>"
                        + "<div style='text-align:center;'>"
                        + "<img width='150' height='150' src='cid:image'>"
                        + "<h1 style='margin-bottom:40px;'>"
                        + "Verification Code"
                        + "</h1>"
                        + "</div>"
                        + "<p>"
                        + "Please use the verification code below to forgot password."
                        + "<br>"
                        + "<h2 style='background: #00466a;width: max-content;padding: 5px 10px;color: #fff;border-radius: 4px;'>" + otp
                        + "</h2>"
                        + "If you did not request this, you can ignore this email."
                        + "<br>"
                        + "<br>"
                        + "Thanks,"
                        + "<br>"
                        + "The IT team"
                        + "</p>"
                        + "</div>"
                        + "<div style='background: #F0F0F0; text-align: center; padding: 30px;margin-top:10px'>"
                        + "Note: Please do not reply to this Email. For any queries please contact your IT Resource Manager."
                        + "</div>"
                        + "</div>";
                String to = email;
                this.emailService.sendEmail(subject, message, to);
                this.resetPasswordService.updateResetPasswordToken(token,email);
                UserOtp userOtp = new UserOtp();
                userOtp.setUser(user);
                userOtp.setOneTimePassword(String.valueOf(otp));
                userOtp.setOtpRequestedTime(new Date());
                userOtpService.createUserOtp(userOtp);
                map.put("resetPasswordToken",token);
                return SuccessResponse.successHandler(HttpStatus.OK, false, "OTP send successfully", map);
            }
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam(value = "otp",required = false) Long otp,@RequestParam("token") String token) {
        try {
            if(otp==null || token==""){
                return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Field can't be empty");
            }else if(this.userOtpRepository.getOneTimePasswordByOneTimePassword(String.valueOf(otp))==null){
                return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid Otp");
            }else{
                UserOtp userOtp = this.userOtpRepository.getOneTimePasswordByOneTimePassword(String.valueOf(otp));
                if (userOtp != null) {
                    String resetPasswordToken = userOtp.getUser().getResetPasswordToken();
                    if(resetPasswordToken.equals(token)){
                        long currentTimeInMillis = System.currentTimeMillis();
                        long otpRequestedTimeInMillis = userOtp.getOtpRequestedTime().getTime();
                        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
                            this.userOtpService.deleteUserOtp(userOtp.getId());
                            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "OTP has been expired");
                        } else {
                            return SuccessResponse.successHandler(HttpStatus.OK, false, "OTP verified successfully", null);
                        }
                    }else {
                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid user");
                    }
                } else {
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "OTP Validation failed");
                }
            }
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword,@RequestParam("token") String token) {
        try{
            if(changePassword.getNewPassword()=="" || changePassword.getConfirmNewPassword()=="" || changePassword.getOldPassword()=="" || token==""){
                return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Field can't be empty");
            }else if(this.userRepository.findByResetPasswordToken(token)==null){
                return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid user");
            }else{
                Boolean data = bCryptPasswordEncoder.matches(changePassword.getOldPassword(),this.userRepository.findByResetPasswordToken(token).getPassword());
                if(data){
                    if(changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword())){
                        User user = this.userRepository.findByResetPasswordToken(token);
                        user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
                        this.userRepository.save(user);
                        return SuccessResponse.successHandler(HttpStatus.OK, false, "Password changed successfully",null);
                    }else{
                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true,"Password must be match");
                    }
                }else{
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Incorrect password");
                }
            }
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
        }
    }

}
