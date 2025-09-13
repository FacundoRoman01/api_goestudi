package com.goestudi.controller;

import com.goestudi.dto.UserProfileDTO;
import com.goestudi.model.User;
import com.goestudi.repository.UserRepository;
import com.goestudi.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

/**
 * Controlador REST para gestionar las operaciones del perfil de usuario.
 */
@RestController
@RequestMapping("/api/v1/profiles/user")
public class UserProfileController {

    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    public UserProfileController(UserProfileService userProfileService, UserRepository userRepository) {
        this.userProfileService = userProfileService;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint para obtener el perfil del usuario autenticado.
     * Asume que el ID del usuario se obtiene de la sesión de autenticación.
     * @param principal Objeto que representa al usuario autenticado.
     * @return ResponseEntity con el DTO del perfil de usuario.
     */
    @GetMapping
    public ResponseEntity<UserProfileDTO> getAuthenticatedUserProfile(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);

        log.info("CONTROLLER - Solicitud para obtener el perfil del usuario autenticado: {}", userId);
        UserProfileDTO userProfile = userProfileService.findByUserId(userId);
        return ResponseEntity.ok(userProfile);
    }
    
    /**
     * Endpoint para crear un nuevo perfil para el usuario autenticado.
     * @param principal Objeto que representa al usuario autenticado.
     * @param userProfileDTO El DTO del perfil de usuario a crear.
     * @return ResponseEntity con el DTO del perfil de usuario creado y el estado HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<UserProfileDTO> createAuthenticatedUserProfile(Principal principal, @RequestBody UserProfileDTO userProfileDTO) {
        Long userId = getUserIdFromPrincipal(principal);
        log.info("CONTROLLER - Solicitud para crear un perfil para el usuario con ID: {}", userId);
        
        UserProfileDTO createdProfile = userProfileService.createProfile(userId, userProfileDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }
    
    // Método auxiliar para obtener el ID del usuario a partir del Principal
    private Long getUserIdFromPrincipal(Principal principal) {
        // Obtenemos el email del usuario autenticado desde el objeto Principal
        String userEmail = principal.getName();
        
        // Buscamos el usuario en la base de datos por su email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
                
        return user.getId();
    }
}
