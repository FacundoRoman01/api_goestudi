	package com.goestudi.model;
	
	import jakarta.persistence.*;
	import java.time.LocalDateTime;
	import java.util.List;
	
	/**
	 * Perfil de la empresa - información pública visible para estudiantes
	 */
	@Entity
	@Table(name = "company_profiles")
	public class CompanyProfile {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    // Relación uno a uno con User (rol COMPANY)
	    @OneToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;
	
	    @Column(nullable = false)
	    private String name; // Nombre de la empresa
	
	    @Column(columnDefinition = "TEXT")
	    private String description; // Descripción de la empresa
	
	    private String location; // Ubicación principal
	
	    private String website; // Sitio web de la empresa
	
	    private String industry; // Industria/sector
	
	    @Column(name = "logo_url")
	    private String logoUrl; // URL del logo de la empresa
	
	    @Column(name = "employee_count")
	    private String employeeCount; // Tamaño de la empresa (ej: "1-50", "51-200")
	
	    @Column(name = "founded_year")
	    private Integer foundedYear; // Año de fundación
	
	    @Column(name = "created_at")
	    private LocalDateTime createdAt; // Fecha de creación del perfil
	
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt; // Última actualización
	
	    @Column(name = "is_verified")
	    private Boolean isVerified = false; // Si la empresa está verificada por admin
	
	    // Lista de trabajos publicados por esta empresa
	    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private List<Job> jobs;
	
	    // Constructores
	    public CompanyProfile() {}
	
	    public CompanyProfile(User user, String name) {
	        this.user = user;
	        this.name = name;
	        this.createdAt = LocalDateTime.now();
	        this.updatedAt = LocalDateTime.now();
	        this.isVerified = false;
	    }
	
	    // Método que se ejecuta antes de persistir
	    @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();
	        if (isVerified == null) {
	            isVerified = false;
	        }
	    }
	
	    // Método que se ejecuta antes de actualizar
	    @PreUpdate
	    protected void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }
	
	    /**
	     * Cuenta el número total de trabajos activos publicados
	     * @return Número de trabajos activos
	     */
	    public long getActiveJobsCount() {
	        if (jobs == null) return 0;
	        return jobs.stream()
	                  .filter(job -> job.getStatus() == Job.JobStatus.ACTIVE)
	                  .count();
	    }
	
	    /**
	     * Cuenta el total de postulaciones recibidas en todos los trabajos
	     * @return Número total de postulaciones
	     */
	    public long getTotalApplicationsCount() {
	        if (jobs == null) return 0;
	        return jobs.stream()
	                  .filter(job -> job.getApplications() != null)
	                  .mapToLong(job -> job.getApplications().size())
	                  .sum();
	    }
	
	    /**
	     * Verifica si el perfil de la empresa está completo
	     * @return true si tiene nombre, descripción y ubicación
	     */
	    public boolean isProfileComplete() {
	        return name != null && !name.trim().isEmpty() &&
	               description != null && !description.trim().isEmpty() &&
	               location != null && !location.trim().isEmpty();
	    }
	
	    /**
	     * Verifica si la empresa puede publicar trabajos
	     * @return true si el perfil está completo
	     */
	    public boolean canPostJobs() {
	        return isProfileComplete();
	    }
	
	    // Getters y Setters
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	
	    public User getUser() { return user; }
	    public void setUser(User user) { this.user = user; }
	
	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }
	
	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }
	
	    public String getLocation() { return location; }
	    public void setLocation(String location) { this.location = location; }
	
	    public String getWebsite() { return website; }
	    public void setWebsite(String website) { this.website = website; }
	
	    public String getIndustry() { return industry; }
	    public void setIndustry(String industry) { this.industry = industry; }
	
	    public String getLogoUrl() { return logoUrl; }
	    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
	
	    public String getEmployeeCount() { return employeeCount; }
	    public void setEmployeeCount(String employeeCount) { this.employeeCount = employeeCount; }
	
	    public Integer getFoundedYear() { return foundedYear; }
	    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }
	
	    public LocalDateTime getCreatedAt() { return createdAt; }
	    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	
	    public LocalDateTime getUpdatedAt() { return updatedAt; }
	    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	
	    public Boolean getIsVerified() { return isVerified; }
	    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
	
	    public List<Job> getJobs() { return jobs; }
	    public void setJobs(List<Job> jobs) { this.jobs = jobs; }
	}