package com.FlickFlow.FlickFlow.user.service;

import com.FlickFlow.FlickFlow.movie.entity.movie;
import com.FlickFlow.FlickFlow.user.dto.roleDto;
import com.FlickFlow.FlickFlow.user.dto.userDto;
import com.FlickFlow.FlickFlow.user.entity.role;
import com.FlickFlow.FlickFlow.user.entity.user;
import com.FlickFlow.FlickFlow.user.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.FlickFlow.FlickFlow.movie.repository.movieRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class userService {
    @Autowired
    private final userRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public userService(userRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }



    public user authenticate(userDto userDto) {
        user user = userRepository.findByEmail(userDto.getEmail());
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    public userDto register(userDto userDTO) {
        if (userDTO.getPassword() != null) {
            if (userRepository.findByEmail(userDTO.getEmail()) != null) {
                throw new RuntimeException("Email already in use");
            }
            user user = new user();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hashing password
            user.setBirthday(userDTO.getBirthday());
            user.setSubscriptionType(userDTO.getSubscriptionType());
            user.setJoinDate(LocalDate.now()); // Set current date as join date
            user.setPreferences(userDTO.getPreferences()); // Set preferences
            user = userRepository.save(user);

            return convertToDTO(user);
        } else {
            throw new IllegalArgumentException("Password must not be null");
        }

    }


    public userDto signIn(String email, String password) {
        user user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return convertToDTO(user);
    }


    public List<userDto> findAll() {
        List<user> users = (List<user>) userRepository.findAll();
        return users.stream().map(this::convertToDTO).toList();
    }

    public userDto findById(int id) {
        Optional<user> user = userRepository.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    public userDto updateUser(int id, userDto userDTO) {
        user user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user = userRepository.save(user);

        return convertToDTO(user);
    }

    public void delete(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }


    private userDto convertToDTO(user user) {
        userDto dto = new userDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

//    public List<movie> getRecommendations(int userId) {
//        user user = userRepository.findByUserId(userId);
//        List<String> preferences = user.getPreferences();
//        return movieRepository.findByGenresIn(preferences);
//    }

}