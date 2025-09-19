package com.goestudi.dto;

import java.time.LocalDateTime;

import com.goestudi.model.JobApplication.ApplicationStatus;

//Para devolver una postulación (respuesta)

public class JobApplicationResponseDTO {
    
    private Long id;
    private String applicantName;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private String coverLetter;
    private JobDTO job;
    private CandidateDTO candidate;


    // Constructores
    public JobApplicationResponseDTO() {}

    public JobApplicationResponseDTO(Long id, String applicantName, String jobTitle, 
                                   String companyName, ApplicationStatus status, 
                                   LocalDateTime appliedAt, LocalDateTime reviewedAt,JobDTO job , String coverLetter, CandidateDTO candidate
                                   ) {
        this.id = id;
        this.applicantName = applicantName;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
        this.appliedAt = appliedAt;
        this.reviewedAt = reviewedAt;
        this.job = job;
        this.coverLetter = coverLetter;
        this.setCandidate(candidate);

    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

	public JobDTO getJob() {
		return job;
	}

	public void setJob(JobDTO job) {
		this.job = job;
	}

	public String getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}

	public CandidateDTO getCandidate() {
		return candidate;
	}

	public void setCandidate(CandidateDTO candidate) {
		this.candidate = candidate;
	}


}