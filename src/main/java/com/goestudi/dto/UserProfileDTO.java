package com.goestudi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para la entidad UserProfile
 */
public class UserProfileDTO {
    private Long id;
    private String fullName;
    private LocalDate fechaNacimiento;
    private String location;
    private String education;
    private String skills;
    private String description;
    private String profilePictureUrl;
    private String cvUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isProfileComplete;

    public UserProfileDTO() {
    }

    public UserProfileDTO(Long id, String fullName, LocalDate fechaNacimiento, String location, String education, String skills, String description, String profilePictureUrl, String cvUrl, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isProfileComplete) {
        this.id = id;
        this.fullName = fullName;
        this.fechaNacimiento = fechaNacimiento;
        this.location = location;
        this.education = education;
        this.skills = skills;
        this.description = description;
        this.profilePictureUrl = profilePictureUrl;
        this.cvUrl = cvUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isProfileComplete = isProfileComplete;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsProfileComplete() {
        return isProfileComplete;
    }

    public void setIsProfileComplete(Boolean isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
    }
}
