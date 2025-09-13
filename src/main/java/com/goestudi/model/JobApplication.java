package com.goestudi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una postulación de un estudiante a un trabajo
 */
@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el perfil del estudiante que se postula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    // Relación con el trabajo al que se postula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt; // Fecha y hora de la postulación

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING; // Estado de la postulación

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter; // Carta de presentación (opcional)

    @Column(columnDefinition = "TEXT")
    private String notes; // Notas internas de la empresa

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt; // Cuándo fue revisada por la empresa

    @Column(name = "reviewed_by")
    private String reviewedBy; // Quién la revisó (email del usuario de la empresa)

    // Estados posibles de una postulación
    public enum ApplicationStatus {
        PENDING,   // Pendiente de revisión
        REVIEWED,  // Revisada por la empresa
        ACCEPTED,  // Aceptada
        REJECTED   // Rechazada
    }

    // Constructores
    public JobApplication() {}

    public JobApplication(UserProfile userProfile, Job job) {
        this.userProfile = userProfile;
        this.job = job;
        this.appliedAt = LocalDateTime.now();
        this.status = ApplicationStatus.PENDING;
    }

    public JobApplication(UserProfile userProfile, Job job, String coverLetter) {
        this.userProfile = userProfile;
        this.job = job;
        this.coverLetter = coverLetter;
        this.appliedAt = LocalDateTime.now();
        this.status = ApplicationStatus.PENDING;
    }

    // Método que se ejecuta antes de persistir
    @PrePersist
    protected void onCreate() {
        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ApplicationStatus.PENDING;
        }
    }

    // Método que se ejecuta antes de actualizar
    @PreUpdate
    protected void onUpdate() {
        // Si el estado cambió de PENDING, marcar como revisada
        if (status != ApplicationStatus.PENDING && reviewedAt == null) {
            reviewedAt = LocalDateTime.now();
        }
    }

    /**
     * Verifica si la postulación está pendiente
     */
    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }

    /**
     * Verifica si la postulación fue aceptada
     */
    public boolean isAccepted() {
        return status == ApplicationStatus.ACCEPTED;
    }

    /**
     * Verifica si la postulación fue rechazada
     */
    public boolean isRejected() {
        return status == ApplicationStatus.REJECTED;
    }

    /**
     * Marca la postulación como revisada
     * @param reviewerEmail Email de quien la revisa
     */
    public void markAsReviewed(String reviewerEmail) {
        this.status = ApplicationStatus.REVIEWED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = reviewerEmail;
    }

    /**
     * Acepta la postulación
     * @param reviewerEmail Email de quien la acepta
     */
    public void accept(String reviewerEmail) {
        this.status = ApplicationStatus.ACCEPTED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = reviewerEmail;
    }

    /**
     * Rechaza la postulación
     * @param reviewerEmail Email de quien la rechaza
     * @param reason Razón del rechazo (opcional)
     */
    public void reject(String reviewerEmail, String reason) {
        this.status = ApplicationStatus.REJECTED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = reviewerEmail;
        if (reason != null) {
            this.notes = reason;
        }
    }

    /**
     * Obtiene información básica del estudiante
     * @return Nombre completo del estudiante
     */
    public String getApplicantName() {
        return userProfile != null ? userProfile.getFullName() : "Desconocido";
    }

    /**
     * Obtiene el título del trabajo
     * @return Título del trabajo
     */
    public String getJobTitle() {
        return job != null ? job.getTitle() : "Trabajo Desconocido";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserProfile getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
}