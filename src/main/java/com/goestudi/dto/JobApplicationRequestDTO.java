package com.goestudi.dto;

// ========================
// DTO PARA CREAR POSTULACIÓN
// ========================
public class JobApplicationRequestDTO {
    
    private Long jobId;
    private String coverLetter; // Opcional

    // Constructores
    public JobApplicationRequestDTO() {}

    public JobApplicationRequestDTO(Long jobId, String coverLetter) {
        this.jobId = jobId;
        this.coverLetter = coverLetter;
    }

    // Getters y Setters
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
}