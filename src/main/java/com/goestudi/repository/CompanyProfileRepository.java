package com.goestudi.repository;

import com.goestudi.model.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    // Buscar perfil por id de usuario (ej: empresa logueada)
    Optional<CompanyProfile> findByUserId(Long userId);

    // Verificar si ya existe un perfil para un usuario
    boolean existsByUserId(Long userId);
}
