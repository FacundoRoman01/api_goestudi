package com.goestudi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class JobDTO {

    private Long id;
    
    @NotBlank(message = "El título no puede estar en blanco")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    private String title;
    
    @NotBlank(message = "La ubicación no puede estar en blanco")
    private String location;

    private String companyName;  
    private Boolean isPaid;
    private Boolean isInternship;
    private Boolean isPartTime;

    @NotBlank(message = "La descripción no puede estar en blanco")
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String description;
    
    @Size(max = 2000, message = "Los detalles del trabajo no pueden tener más de 2000 caracteres")
    private String jobDetails;
    
    @Size(max = 2000, message = "Los requerimientos no pueden tener más de 2000 caracteres")
    private String requirements; //lista de requerimiento

    private BigDecimal salary;

    private LocalDateTime postedAt;
    private LocalDateTime deadline;

    private String postedAgo;
    
    private String status;

    // Constructor vacío
    public JobDTO() {}

    // Constructor completo
    public JobDTO(Long id, String title, String location, String companyName,
                  Boolean isPaid, Boolean isInternship, Boolean isPartTime,
                  String description, String jobDetails, String requirements,
                  BigDecimal salary, LocalDateTime postedAt, LocalDateTime deadline,
                  String postedAgo, String status) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.companyName = companyName;
        this.isPaid = isPaid;
        this.isInternship = isInternship;
        this.isPartTime = isPartTime;
        this.description = description;
        this.jobDetails = jobDetails;
        this.requirements = requirements;
        this.salary = salary;
        this.postedAt = postedAt;
        this.deadline = deadline;
        this.postedAgo = postedAgo;
        this.status = status;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }

    public Boolean getIsInternship() { return isInternship; }
    public void setIsInternship(Boolean isInternship) { this.isInternship = isInternship; }

    public Boolean getIsPartTime() { return isPartTime; }
    public void setIsPartTime(Boolean isPartTime) { this.isPartTime = isPartTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getJobDetails() { return jobDetails; }
    public void setJobDetails(String jobDetails) { this.jobDetails = jobDetails; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public String getPostedAgo() { return postedAgo; }
    public void setPostedAgo(String postedAgo) { this.postedAgo = postedAgo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
