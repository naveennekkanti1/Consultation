package org.example.controller;

import org.example.constants.responsecode;
import org.example.entity.UserModel;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class Auth {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        Optional<UserModel> existingUser = userRepository.findByEmail(email);
        if (!existingUser.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", responsecode.UNAUTHORIZED);
            errorResponse.put("message", "Invalid credentials or user not found");
            return ResponseEntity
                    .status(responsecode.UNAUTHORIZED)
                    .body(errorResponse);
        }
        UserModel user = existingUser.get();
        if (!user.getPassword().equals(password)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", responsecode.UNAUTHORIZED);
            errorResponse.put("message", "Invalid email or password");
            return ResponseEntity
                    .status(responsecode.UNAUTHORIZED)
                    .body(errorResponse);
        }
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("email", user.getEmail());
        userMap.put("role", "admin");
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("code", responsecode.SUCCESS);
        successResponse.put("message", "Admin login successful");
        successResponse.put("user", userMap);
        return ResponseEntity
                .status(responsecode.SUCCESS)
                .body(successResponse);
    }
}
