package com.goestudi.repository;

import com.goestudi.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad UserProfile.
 * Se encarga de la persistencia de los datos del perfil de estudiante.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Busca un perfil de usuario por el ID del usuario asociado.
     * @param userId El ID del usuario.
     * @return Un Optional que contiene el UserProfile si se encuentra, o vacío si no.
     */
    Optional<UserProfile> findByUserId(Long userId);
}
