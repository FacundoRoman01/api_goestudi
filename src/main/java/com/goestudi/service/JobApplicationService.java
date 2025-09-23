package com.goestudi.service;

import com.goestudi.dto.*;
import com.goestudi.model.*;
import com.goestudi.repository.CompanyProfileRepository;
import com.goestudi.repository.JobApplicationRepository;
import com.goestudi.repository.JobRepository;
import com.goestudi.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;


@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserProfileRepository userProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JobRepository jobRepository,
                                 UserProfileRepository userProfileRepository, CompanyProfileRepository companyProfileRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.userProfileRepository = userProfileRepository;
        this.companyProfileRepository = companyProfileRepository;
    }

    @Transactional
    public JobApplicationResponseDTO apply(Long userId, JobApplicationRequestDTO dto) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new RuntimeException("Trabajo no encontrado"));

        JobApplication application = new JobApplication(userProfile, job, dto.getCoverLetter());
        jobApplicationRepository.save(application);

        return mapToResponse(application);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getUserApplications(Long userId) {
        return jobApplicationRepository.findByUserProfileId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public JobApplicationResponseDTO updateStatus(Long applicationId, ApplicationStatusUpdateDTO dto, String reviewerEmail) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));

        switch (dto.getStatus()) {
            case ACCEPTED -> application.accept(reviewerEmail);
            case REJECTED -> application.reject(reviewerEmail, dto.getNotes());
            case REVIEWED -> application.markAsReviewed(reviewerEmail);
            case PENDING -> throw new RuntimeException("No podés volver a PENDING una aplicación");
        }

        jobApplicationRepository.save(application);
        return mapToResponse(application);
    }


    private JobApplicationResponseDTO mapToResponse(JobApplication application) {
        JobApplicationResponseDTO dto = new JobApplicationResponseDTO();
        dto.setId(application.getId());
        dto.setApplicantName(application.getApplicantName());
        dto.setJobTitle(application.getJobTitle());
        dto.setCompanyName(application.getJob().getCompanyName());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setCoverLetter(application.getCoverLetter());
        dto.setReviewedAt(application.getReviewedAt());
     // ✅ Info del CV del candidato
        dto.setCvFilename(application.getCvFilename());        // nombre del archivo
        dto.setCvContentType(application.getCvContentType());  // tipo MIME, ej: application/pdf
        dto.setHasCv(application.getCvFile() != null);         // true si tiene archivo

        
        

        // Info del Job
        JobDTO jobDto = new JobDTO();
        jobDto.setId(application.getJob().getId());
        jobDto.setTitle(application.getJob().getTitle());
        jobDto.setCompanyName(application.getJob().getCompanyName());
        jobDto.setLocation(application.getJob().getLocation());
        jobDto.setIsPaid(application.getJob().getIsPaid());
        jobDto.setIsInternship(application.getJob().getIsInternship());
        jobDto.setIsPartTime(application.getJob().getIsPartTime());
        jobDto.setDescription(application.getJob().getDescription());
        jobDto.setJobDetails(application.getJob().getJobDetails());
        jobDto.setRequirements(application.getJob().getRequirements());
        jobDto.setSalary(application.getJob().getSalary());
        jobDto.setPostedAgo(application.getJob().getPostedAgo());
        jobDto.setDeadline(application.getJob().getDeadline());
        dto.setJob(jobDto);

        // ✅ Info del candidato
        UserProfile user = application.getUserProfile(); // tu entidad que referencia al postulante
        CandidateDTO candidate = new CandidateDTO();
        candidate.setId(user.getId());
        candidate.setFullName(user.getFullName());
        candidate.setEmail(user.getUser().getEmail()); // si tu UserProfile tiene User con email
        candidate.setLocation(user.getLocation());
    
        List<String> skillsList = new ArrayList<>();
        if (user.getSkills() != null && !user.getSkills().trim().isEmpty()) {
            String[] skillsArray = user.getSkills().split(",");
            for (String skill : skillsArray) {
                skillsList.add(skill.trim());
            }
        }
        candidate.setSkills(skillsList); // suponiendo que skills es una lista de objetos con getName()
        
        dto.setCandidate(candidate);

        return dto;
    }


    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getAllApplications() {
        return jobApplicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una postulación específica por ID
     */
    @Transactional(readOnly = true)
    public JobApplicationResponseDTO getApplicationById(Long id) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));
        
        return mapToResponse(application);
    }
    
    
    
    @Transactional
    public JobApplicationResponseDTO applyByEmail(String email, JobApplicationRequestDTO dto) {
        UserProfile userProfile = userProfileRepository.findByUser_Email(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Job job = jobRepository.findById(dto.getJobId())
            .orElseThrow(() -> new RuntimeException("Trabajo no encontrado"));

        JobApplication application = new JobApplication(userProfile, job, dto.getCoverLetter());
        jobApplicationRepository.save(application);

        return mapToResponse(application);
    }
    
 // AGREGAR este método a tu JobApplicationService
    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getApplicationsByEmail(String email) {
        UserProfile userProfile = userProfileRepository.findByUser_Email(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Usar tu método existente
        return getUserApplications(userProfile.getId());
    }
    
    
    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getApplicationsByCompanyEmail(String companyEmail) {
        // 1. Buscar la empresa logueada por email de usuario
        CompanyProfile company = companyProfileRepository.findByUser_Email(companyEmail)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // 2. Buscar todos los trabajos de esa empresa
        List<Job> companyJobs = jobRepository.findByCompanyProfile(company);
        if (companyJobs.isEmpty()) return List.of();

        // 3. Buscar postulaciones asociadas a esos trabajos
        return jobApplicationRepository.findByJobIn(companyJobs)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getApplicationsByJob(Long jobId) {
        return jobApplicationRepository.findByJobId(jobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    
    public JobApplication getApplicationEntityById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));
    }

    
    

    @Transactional
    public JobApplicationResponseDTO applyWithCv(String email, JobApplicationRequestDTO dto, MultipartFile cvFile) {
        UserProfile userProfile = userProfileRepository.findByUser_Email(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Job job = jobRepository.findById(dto.getJobId())
            .orElseThrow(() -> new RuntimeException("Trabajo no encontrado"));

        JobApplication application = new JobApplication(userProfile, job, dto.getCoverLetter());

        if (cvFile != null && !cvFile.isEmpty()) {
            try {
                application.setCvFile(cvFile.getBytes());
                application.setCvFilename(cvFile.getOriginalFilename());
                application.setCvContentType(cvFile.getContentType());
            } catch (Exception e) {
                throw new RuntimeException("Error guardando el archivo", e);
            }
        }

        jobApplicationRepository.save(application);
        return mapToResponse(application);
    }

    
    
    
}
