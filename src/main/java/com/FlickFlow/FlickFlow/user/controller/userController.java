package com.FlickFlow.FlickFlow.user.controller;

import com.FlickFlow.FlickFlow.user.Request.LoginRequest;
import com.FlickFlow.FlickFlow.user.Request.SignInRequest;
import com.FlickFlow.FlickFlow.user.dto.userDto;
import com.FlickFlow.FlickFlow.user.entity.user;
import com.FlickFlow.FlickFlow.user.service.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class userController {

    @Autowired
    private userService userService;
    private static final Logger log = LoggerFactory.getLogger(userController.class);


//    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
//    public userDto registerUser(@RequestBody userDto userDto) {
//        return userService.register(userDto);
//    }
@PostMapping("/signup")
public ResponseEntity<?> registerUser(@RequestBody userDto userDto) {
    try {
        log.info("Registering user: " + userDto);
        userService.register(userDto);
//        return ResponseEntity.ok().body("User registered successfully");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("User registered successfully"));
    } catch (Exception e) {
        log.error("Error registering user", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
    }
}

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody userDto userDto) {
        try {
            user user = userService.authenticate(userDto);
            // Return a JSON response
            return ResponseEntity.ok(new ResponseMessage("User signed in successfully"));
        } catch (RuntimeException e) {
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(e.getMessage()));
        }
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<userDto> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public userDto findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public userDto updateUser(@PathVariable int id, @RequestBody userDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
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

    class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
