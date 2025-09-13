package com.goestudi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Perfil del estudiante - almacena datos personales y académicos
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación uno a uno con User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "full_name")
    private String fullName; // Nombre completo

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento; // Fecha de nacimiento

    private String location; // Ubicación/ciudad

    @Column(columnDefinition = "TEXT")
    private String education; // Información académica

    @Column(columnDefinition = "TEXT")
    private String skills; // Habilidades del estudiante

    @Column(columnDefinition = "TEXT")
    private String description; // Descripción personal

    @Column(name = "profile_picture_url")
    private String profilePictureUrl; // URL de la foto de perfil

    @Column(name = "cv_url")
    private String cvUrl; // URL del CV

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Fecha de creación del perfil

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Última actualización

    @Column(name = "is_profile_complete")
    private Boolean isProfileComplete = false; // Si el perfil está completo

    // Lista de postulaciones realizadas por este usuario
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobApplication> applications;

    // Constructores
    public UserProfile() {}

    public UserProfile(User user, String fullName) {
        this.user = user;
        this.fullName = fullName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isProfileComplete = false;
    }

    // Método que se ejecuta antes de persistir
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isProfileComplete == null) {
            isProfileComplete = false;
        }
    }

    // Método que se ejecuta antes de actualizar
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Auto-detectar si el perfil está completo
        checkProfileCompleteness();
    }

    /**
     * Verifica automáticamente si el perfil está completo
     * Un perfil se considera completo si tiene: nombre, ubicación, educación y descripción
     */
    private void checkProfileCompleteness() {
        this.isProfileComplete = fullName != null && !fullName.trim().isEmpty() &&
                                 location != null && !location.trim().isEmpty() &&
                                 education != null && !education.trim().isEmpty() &&
                                 description != null && !description.trim().isEmpty();
    }

    /**
     * Calcula la edad basada en la fecha de nacimiento
     * @return Edad en años o null si no hay fecha de nacimiento
     */
    public Integer getAge() {
        if (fechaNacimiento == null) return null;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    /**
     * Verifica si el usuario puede postularse a trabajos
     * @return true si el perfil está completo
     */
    public boolean canApplyToJobs() {
        return Boolean.TRUE.equals(isProfileComplete);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getCvUrl() { return cvUrl; }
    public void setCvUrl(String cvUrl) { this.cvUrl = cvUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getIsProfileComplete() { return isProfileComplete; }
    public void setIsProfileComplete(Boolean isProfileComplete) { this.isProfileComplete = isProfileComplete; }

    public List<JobApplication> getApplications() { return applications; }
    public void setApplications(List<JobApplication> applications) { this.applications = applications; }
}