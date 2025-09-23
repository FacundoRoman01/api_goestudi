// src/main/java/com/goestudi/model/Job.java

package com.goestudi.model;



import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Enumerated(EnumType.STRING)  // 👈 guardamos el enum como texto
    @Column(nullable = false)
    private Location location;
    
    // CAMBIO IMPORTANTE: Reemplazar 'company' String por relación con CompanyProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "is_internship")
    private Boolean isInternship;

    @Column(name = "is_part_time")
    private Boolean isPartTime;

    // NUEVOS CAMPOS 
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "job_details", columnDefinition = "TEXT")
    private String jobDetails; // Mantengo tu campo original

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private BigDecimal salary; // Opcional

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE; // Por defecto ACTIVE

    // CAMBIO: Reemplazar postedAgo String por cálculo automático
   

    // Relación con JobApplication (las postulaciones a este trabajo)
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobApplication> applications;

    // Enum para status
    public enum JobStatus {
        ACTIVE, CLOSED, DRAFT
    }

    // Constructores
    public Job() {}

    public Job(String title, Location location, CompanyProfile companyProfile, 
               Boolean isPaid, Boolean isInternship, Boolean isPartTime, 
               String description, String requirements) {
        this.title = title;
        this.location = location;
        this.companyProfile = companyProfile;
        this.isPaid = isPaid;
        this.isInternship = isInternship;
        this.isPartTime = isPartTime;
        this.description = description;
        this.requirements = requirements;
        this.postedAt = LocalDateTime.now();
        this.status = JobStatus.ACTIVE;
    }

    // Método para calcular "posted ago" dinámicamente
    public String getPostedAgo() {
        if (postedAt == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        long days = java.time.Duration.between(postedAt, now).toDays();
        
        if (days == 0) return "Today";
        if (days == 1) return "1 day ago";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " week" + (days / 7 > 1 ? "s" : "") + " ago";
        return (days / 30) + " month" + (days / 30 > 1 ? "s" : "") + " ago";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public CompanyProfile getCompanyProfile() { return companyProfile; }
    public void setCompanyProfile(CompanyProfile companyProfile) { this.companyProfile = companyProfile; }

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

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public List<JobApplication> getApplications() { return applications; }
    public void setApplications(List<JobApplication> applications) { this.applications = applications; }

    /**
     * Obtiene el nombre de la empresa de forma segura
     * @return Nombre de la empresa o "Empresa Desconocida" si no existe
     */
    public String getCompanyName() {
        return companyProfile != null ? companyProfile.getName() : "Empresa Desconocida";
    }
}