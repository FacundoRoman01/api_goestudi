package com.goestudi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goestudi.dto.AuthResponseDTO;
import com.goestudi.dto.MessageResponseDTO;
import com.goestudi.dto.UserDTO;
import com.goestudi.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.ok(new MessageResponseDTO("Usuario registrado exitosamente"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            AuthResponseDTO response = userService.login(userDTO.getEmail(), userDTO.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                Map.of("error", e.getMessage())
            );
        }
    }
}
