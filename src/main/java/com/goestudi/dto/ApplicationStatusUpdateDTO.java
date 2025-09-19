package com.goestudi.dto;

import com.goestudi.model.JobApplication;
import com.goestudi.model.JobApplication.ApplicationStatus;

public class ApplicationStatusUpdateDTO {
    
    private JobApplication.ApplicationStatus status;
    private String notes; // Opcional, para rechazos

    // Constructores
    public ApplicationStatusUpdateDTO() {}

    public ApplicationStatusUpdateDTO(ApplicationStatus status, String notes) {
        this.status = status;
        this.notes = notes;
    }

    // Getters y Setters
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}