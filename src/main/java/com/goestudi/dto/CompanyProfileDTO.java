package com.goestudi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDateTime;
import java.util.List;


public class CompanyProfileDTO {
    private Long id;
    
    @NotBlank(message = "El nombre de la empresa no puede estar vacío.")
    @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres.")
    private String name;
    
    @NotBlank(message = "La descripción de la empresa no puede estar vacía.")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres.")
    private String description;
    
    @NotBlank(message = "La ubicación no puede estar vacía.")
    private String location;
    
    @URL(message = "El sitio web debe ser una URL válida.")
    private String website;
    
    private String industry;
    
    @URL(message = "La URL del logo debe ser válida.")
    private String logoUrl;
    
    private String employeeCount;
    
    private Integer foundedYear;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Boolean isVerified;
    
    private List<JobDTO> jobs;

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

	public List<JobDTO> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobDTO> jobs) {
		this.jobs = jobs;
	}
}
