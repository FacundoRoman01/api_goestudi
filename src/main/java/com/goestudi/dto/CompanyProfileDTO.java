package com.goestudi.dto;

import java.time.LocalDateTime;

/**
 * DTO para la entidad CompanyProfile, sin usar anotaciones de Lombok.
 */
public class CompanyProfileDTO {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String website;
    private String industry;
    private String logoUrl;
    private String employeeCount;
    private Integer foundedYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isVerified;

    public CompanyProfileDTO() {
    }

    public CompanyProfileDTO(Long id, String name, String description, String location, String website, String industry, String logoUrl, String employeeCount, Integer foundedYear, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isVerified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.website = website;
        this.industry = industry;
        this.logoUrl = logoUrl;
        this.employeeCount = employeeCount;
        this.foundedYear = foundedYear;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isVerified = isVerified;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
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

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
