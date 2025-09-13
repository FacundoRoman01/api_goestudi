package com.goestudi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goestudi.dto.UserDTO;
import com.goestudi.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            String token = userService.login(userDTO.getEmail(), userDTO.getPassword());

            // Devuelvo un JSON con el token y el rol
            return ResponseEntity.ok(
                java.util.Map.of(
                    "token", token,
                    "role", "USER" // 👈 si tenés roles dinámicos, trae el real desde UserService
                )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                java.util.Map.of("error", e.getMessage())
            );
        }
    }

}
