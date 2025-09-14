package com.goestudi.controller;

import com.goestudi.dto.CompanyProfileDTO;
import com.goestudi.model.CompanyProfile;
import com.goestudi.model.User;
import com.goestudi.service.CompanyProfileService;
import com.goestudi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/company-profiles")
public class CompanyProfileController {

    private static final Logger log = LoggerFactory.getLogger(CompanyProfileController.class);

    private final CompanyProfileService companyProfileService;
    private final UserService userService;

    @Autowired
    public CompanyProfileController(CompanyProfileService companyProfileService, UserService userService) {
        this.companyProfileService = companyProfileService;
        this.userService = userService;
    }

    /**
     * Obtiene el perfil de la empresa del usuario autenticado.
     * Endpoint: GET /api/company-profiles/me
     * @param userDetails Los detalles del usuario autenticado proporcionados por Spring Security.
     * @return ResponseEntity con el DTO del perfil de empresa o un estado 404 si no se encuentra.
     */
    @GetMapping("/meCompany")
    public ResponseEntity<CompanyProfileDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("CONTROLLER - Buscando perfil para el usuario autenticado: {}", userDetails.getUsername());
        
        Optional<User> userOptional = userService.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        Optional<CompanyProfile> profileOptional = companyProfileService.findByUser(user);
        
        return profileOptional.map(profile -> {
            log.info("CONTROLLER - Perfil de empresa encontrado para el usuario: {}", user.getEmail());
            return new ResponseEntity<>(companyProfileService.convertToDto(profile), HttpStatus.OK);
        }).orElseGet(() -> {
            log.info("CONTROLLER - Perfil de empresa no encontrado para el usuario: {}", user.getEmail());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     * Crea o actualiza el perfil de la empresa del usuario autenticado.
     * Endpoint: POST /api/company-profiles/me
     * @param userDetails Los detalles del usuario autenticado.
     * @param companyProfileDTO El DTO con los datos del perfil de empresa.
     * @return ResponseEntity con el DTO del perfil guardado o un estado 404 si el usuario no existe.
     */
    @PostMapping("/meCompany")
    public ResponseEntity<CompanyProfileDTO> createOrUpdateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CompanyProfileDTO companyProfileDTO) {
        
        log.info("CONTROLLER - Creando/actualizando perfil para el usuario autenticado: {}", userDetails.getUsername());
        
        Optional<User> userOptional = userService.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        CompanyProfileDTO savedProfile = companyProfileService.createOrUpdateProfile(user, companyProfileDTO);
        log.info("CONTROLLER - Perfil guardado con éxito para el usuario: {}", user.getEmail());
        return new ResponseEntity<>(savedProfile, HttpStatus.OK);
    }
}
