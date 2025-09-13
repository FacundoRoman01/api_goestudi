package com.goestudi.service;

import com.goestudi.dto.UserProfileDTO;
import com.goestudi.exception.UserProfileAlreadyExistsException;
import com.goestudi.exception.UserProfileNotFoundException;
import com.goestudi.model.User;
import com.goestudi.model.UserProfile;
import com.goestudi.repository.UserProfileRepository;
import com.goestudi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * Servicio para gestionar la lógica de negocio del UserProfile.
 * Contiene los métodos para crear, obtener y actualizar perfiles de usuario.
 */
@Service
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository; // Añade la inyección del UserRepository

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Encuentra el DTO de perfil de usuario por el ID del usuario asociado.
     * @param userId El ID del usuario.
     * @return El DTO del perfil de usuario.
     * @throws UserProfileNotFoundException si el perfil no existe.
     */
    public UserProfileDTO findByUserId(Long userId) {
        log.info("SERVICE - Buscando perfil de usuario para el ID: {}", userId);

        Optional<UserProfile> optionalProfile = userProfileRepository.findByUserId(userId);

        UserProfile userProfile = optionalProfile.orElseGet(() -> {
            log.info("No se encontró perfil, creando uno nuevo para el usuario {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            UserProfile newProfile = new UserProfile();
            newProfile.setUser(user);
            return userProfileRepository.save(newProfile);
        });

        return convertToDto(userProfile);
    }

    
    /**
     * Crea un nuevo perfil de usuario.
     * @param userId El ID del usuario.
     * @param userProfileDTO El DTO del perfil de usuario con los datos a crear.
     * @return El DTO del perfil de usuario creado.
     * @throws UserProfileAlreadyExistsException si ya existe un perfil para el usuario.
     */
    public UserProfileDTO createProfile(Long userId, UserProfileDTO userProfileDTO) {
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);

        if (existingProfile.isPresent()) {
            // Actualizar el perfil existente en vez de crear uno nuevo
            UserProfile profile = existingProfile.get();
            profile.setFullName(userProfileDTO.getFullName());
            profile.setFechaNacimiento(userProfileDTO.getFechaNacimiento());
            profile.setLocation(userProfileDTO.getLocation());
            profile.setEducation(userProfileDTO.getEducation());
            profile.setSkills(userProfileDTO.getSkills());
            profile.setDescription(userProfileDTO.getDescription());
            profile.setProfilePictureUrl(userProfileDTO.getProfilePictureUrl());
            profile.setCvUrl(userProfileDTO.getCvUrl());

            return convertToDto(userProfileRepository.save(profile));
        }

        // Crear uno nuevo si no existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        UserProfile userProfile = convertToEntity(userProfileDTO);
        userProfile.setUser(user);
        return convertToDto(userProfileRepository.save(userProfile));
    }


    /**
     * Convierte una entidad UserProfile a un DTO.
     * @param userProfile La entidad a convertir.
     * @return El DTO correspondiente.
     */
    private UserProfileDTO convertToDto(UserProfile userProfile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(userProfile.getId());
        dto.setFullName(userProfile.getFullName());
        dto.setFechaNacimiento(userProfile.getFechaNacimiento());
        dto.setLocation(userProfile.getLocation());
        dto.setEducation(userProfile.getEducation());
        dto.setSkills(userProfile.getSkills());
        dto.setDescription(userProfile.getDescription());
        dto.setProfilePictureUrl(userProfile.getProfilePictureUrl());
        dto.setCvUrl(userProfile.getCvUrl());
        dto.setCreatedAt(userProfile.getCreatedAt());
        dto.setUpdatedAt(userProfile.getUpdatedAt());
        dto.setIsProfileComplete(userProfile.getIsProfileComplete());
        return dto;
    }
    
    /**
     * Convierte un DTO de perfil de usuario a una entidad UserProfile.
     * @param userProfileDTO El DTO a convertir.
     * @return La entidad correspondiente.
     */
    private UserProfile convertToEntity(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = new UserProfile();
        // Nota: El ID no se establece aquí, ya que la base de datos lo genera
        userProfile.setFullName(userProfileDTO.getFullName());
        userProfile.setFechaNacimiento(userProfileDTO.getFechaNacimiento());
        userProfile.setLocation(userProfileDTO.getLocation());
        userProfile.setEducation(userProfileDTO.getEducation());
        userProfile.setSkills(userProfileDTO.getSkills());
        userProfile.setDescription(userProfileDTO.getDescription());
        userProfile.setProfilePictureUrl(userProfileDTO.getProfilePictureUrl());
        userProfile.setCvUrl(userProfileDTO.getCvUrl());
        return userProfile;
    }
}
