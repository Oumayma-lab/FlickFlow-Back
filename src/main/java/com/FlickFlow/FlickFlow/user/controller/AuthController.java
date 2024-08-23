package com.FlickFlow.FlickFlow.user.controller;

import com.FlickFlow.FlickFlow.config.JwtUtil;
import com.FlickFlow.FlickFlow.user.Request.JwtResponse;
import com.FlickFlow.FlickFlow.user.dto.userDto;
import com.FlickFlow.FlickFlow.user.entity.user;
import com.FlickFlow.FlickFlow.user.service.MyUserDetailsService;
import com.FlickFlow.FlickFlow.user.service.userService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.FlickFlow.FlickFlow.user.dto.userDto;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private userService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> tokenDetails) {
        String refreshToken = tokenDetails.get("refreshToken");

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken, userDetailsService.loadUserByUsername(jwtUtil.extractUsername(refreshToken)))) {
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody userDto userDto) {
        log.debug("SignIn request received for email: {}", userDto.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails);
            log.info("User {} signed in successfully", userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername()));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            throw e;
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during authentication: " + e.getMessage());
        }
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody userDto userDto) {
        try {
            log.info("Registering user: " + userDto);
            userService.register(userDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthController.ApiResponse("User registered successfully"));
        } catch (Exception e) {
            log.error("Error registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        log.info("User logged out successfully");
        return ResponseEntity.ok("User logged out successfully");
    }



    public static class ApiResponse {
        private String message;

        public ApiResponse(String message) {
            this.message = message;
        }

        // Getter and Setter
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }



}
