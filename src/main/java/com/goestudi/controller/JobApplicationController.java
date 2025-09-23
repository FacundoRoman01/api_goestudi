package com.goestudi.controller;

import com.goestudi.dto.*;
import com.goestudi.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/apply")
    public ResponseEntity<JobApplicationResponseDTO> apply(
        @RequestBody JobApplicationRequestDTO request,
        Authentication authentication
    ) {
        String email = authentication.getName();
        JobApplicationResponseDTO response = jobApplicationService.applyByEmail(email, request);
        return ResponseEntity.ok(response);
    }
    
    
    
    @PostMapping(value = "/apply-with-cv", consumes = {"multipart/form-data"})
    public ResponseEntity<JobApplicationResponseDTO> applyWithCv(
            @RequestPart("request") JobApplicationRequestDTO request,
            @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
            Authentication authentication
    ) {
        String email = authentication.getName();
        JobApplicationResponseDTO response = jobApplicationService.applyWithCv(email, request, cvFile);
        return ResponseEntity.ok(response);
    }

    
    
    
    @GetMapping("/{id}/cv")
    public ResponseEntity<byte[]> downloadCv(@PathVariable Long id) {
        var application = jobApplicationService.getApplicationEntityById(id);

        if (application.getCvFile() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + application.getCvFilename() + "\"")
                .header("Content-Type", application.getCvContentType())
                .body(application.getCvFile());
    }

    
    
    
    
 // AGREGAR este método a tu JobApplicationController
    @GetMapping("/my-applications")
    public ResponseEntity<List<JobApplicationResponseDTO>> getMyApplications(
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        List<JobApplicationResponseDTO> applications = jobApplicationService.getApplicationsByEmail(userEmail);
        return ResponseEntity.ok(applications);
    }
    
    
    

    // Obtener todas las postulaciones de un usuario
    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getUserApplications(
            @PathVariable Long userId
    ) {
        List<JobApplicationResponseDTO> applications = jobApplicationService.getUserApplications(userId);
        return ResponseEntity.ok(applications);
    }

    // Obtener UNA postulación por id
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDTO> getApplicationById(
            @PathVariable Long id
    ) {
        JobApplicationResponseDTO application = jobApplicationService.getApplicationById(id);
        return ResponseEntity.ok(application);
    }

    // Obtener TODAS las postulaciones (ej: para admin)
    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAllApplications() {
        List<JobApplicationResponseDTO> applications = jobApplicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    // Actualizar estado de una postulación
    @PutMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody ApplicationStatusUpdateDTO dto,
            Authentication authentication
    ) {
        String reviewerEmail = authentication.getName(); // ✅ Seguro
        JobApplicationResponseDTO updated = jobApplicationService.updateStatus(id, dto, reviewerEmail);
        return ResponseEntity.ok(updated);
    }
    
    
 // Candidatos de todos los trabajos de la empresa logueada
    @GetMapping("/company/candidates")
    public ResponseEntity<List<JobApplicationResponseDTO>> getCompanyCandidates(Authentication authentication) {
        String companyEmail = authentication.getName(); // Email de la empresa logueada
        List<JobApplicationResponseDTO> applications = jobApplicationService.getApplicationsByCompanyEmail(companyEmail);
        return ResponseEntity.ok(applications);
    }


    // Candidatos de un trabajo específico
    @GetMapping("/job/{jobId}/candidates")
    public ResponseEntity<List<JobApplicationResponseDTO>> getJobCandidates(@PathVariable Long jobId) {
        List<JobApplicationResponseDTO> applications = jobApplicationService.getApplicationsByJob(jobId);
        return ResponseEntity.ok(applications);
    }

    
    
}
