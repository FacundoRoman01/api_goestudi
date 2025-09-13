package com.goestudi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goestudi.dto.JobDTO;
import com.goestudi.model.Job;
import com.goestudi.model.CompanyProfile;
import com.goestudi.repository.JobRepository;
import com.goestudi.repository.CompanyProfileRepository;
import com.goestudi.specification.JobSpecification;
import com.goestudi.exception.JobNotFoundException;

import jakarta.validation.Valid;

@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final CompanyProfileRepository companyProfileRepository;

    public JobService(JobRepository jobRepository, CompanyProfileRepository companyProfileRepository) {
        this.jobRepository = jobRepository;
        this.companyProfileRepository = companyProfileRepository;
    }

    /**
     * Finds jobs based on filters and returns them in a paginated DTO format.
     */
    public Page<JobDTO> findJobsByFilters(
        String keyword,
        String location,
        Boolean isInternship,
        Boolean isPartTime,
        Pageable pageable) throws Exception {

        log.debug("SERVICE - Parámetros: keyword='{}', location='{}', isInternship={}, isPartTime={}",
                keyword, location, isInternship, isPartTime);

        try {
            Thread.sleep(1500); // Simulated delay for demonstration

            Page<Job> jobPage = jobRepository.findAll(
                JobSpecification.findByFilters(keyword, location, isInternship, isPartTime),
                pageable
            );

            log.info("SERVICE - Trabajos encontrados en DB: {}", jobPage.getTotalElements());

            Page<JobDTO> dtoPage = jobPage.map(this::convertToDto);
            log.info("SERVICE - DTOs convertidos: {}", dtoPage.getContent().size());

            return dtoPage;

        } catch (Exception e) {
            log.error("ERROR en SERVICE: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Creates a new job based on the provided DTO.
     */
    @Transactional
    public JobDTO createJob(JobDTO jobDTO) {
        log.info("SERVICE - Creando un nuevo trabajo con título: {}", jobDTO.getTitle());

        // 1. Convertir DTO a entidad Job
        Job job = new Job();
        job.setTitle(jobDTO.getTitle());
        job.setLocation(jobDTO.getLocation());
        job.setIsPaid(jobDTO.getIsPaid() != null ? jobDTO.getIsPaid() : false);
        job.setIsInternship(jobDTO.getIsInternship() != null ? jobDTO.getIsInternship() : false);
        job.setIsPartTime(jobDTO.getIsPartTime() != null ? jobDTO.getIsPartTime() : false);
        job.setDescription(jobDTO.getDescription());
        job.setJobDetails(jobDTO.getJobDetails());
        job.setRequirements(jobDTO.getRequirements());
        job.setSalary(jobDTO.getSalary());

        // 2. Relacionar con la entidad CompanyProfile
        if (jobDTO.getCompanyId() != null) {
            CompanyProfile companyProfile = companyProfileRepository.findById(jobDTO.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + jobDTO.getCompanyId()));
            job.setCompanyProfile(companyProfile);
        } else {
            throw new IllegalArgumentException("Company ID is required to create a job.");
        }

        // 3. Guardar en la base de datos
        Job savedJob = jobRepository.save(job);
        log.info("SERVICE - Trabajo creado exitosamente con ID: {}", savedJob.getId());

        // 4. Convertir la entidad guardada a DTO y retornarla
        return convertToDto(savedJob);
    }

    /**
     * Updates an existing job.
     * @param id The ID of the job to update.
     * @param jobDTO The DTO with the updated job information.
     * @return The updated JobDTO.
     */
    @Transactional
    public JobDTO updateJob(Long id, @Valid JobDTO jobDTO) {
        log.info("SERVICE - Actualizando trabajo con ID: {}", id);

        // 1. Encontrar el trabajo existente. Si no se encuentra, lanzar una excepción.
        Job existingJob = jobRepository.findById(id)
            .orElseThrow(() -> new JobNotFoundException("Job not found with ID: " + id));

        // 2. Actualizar los campos de la entidad con la información del DTO.
        existingJob.setTitle(jobDTO.getTitle());
        existingJob.setLocation(jobDTO.getLocation());
        existingJob.setIsPaid(jobDTO.getIsPaid() != null ? jobDTO.getIsPaid() : false);
        existingJob.setIsInternship(jobDTO.getIsInternship() != null ? jobDTO.getIsInternship() : false);
        existingJob.setIsPartTime(jobDTO.getIsPartTime() != null ? jobDTO.getIsPartTime() : false);
        existingJob.setDescription(jobDTO.getDescription());
        existingJob.setJobDetails(jobDTO.getJobDetails());
        existingJob.setRequirements(jobDTO.getRequirements());
        existingJob.setSalary(jobDTO.getSalary());

        // 3. Opcionalmente, actualizar la relación con la compañía si el ID de la compañía ha cambiado.
        if (jobDTO.getCompanyId() != null && !jobDTO.getCompanyId().equals(existingJob.getCompanyProfile().getId())) {
            CompanyProfile companyProfile = companyProfileRepository.findById(jobDTO.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + jobDTO.getCompanyId()));
            existingJob.setCompanyProfile(companyProfile);
        }

        // 4. Guardar los cambios.
        Job updatedJob = jobRepository.save(existingJob);
        log.info("SERVICE - Trabajo actualizado exitosamente con ID: {}", updatedJob.getId());
        
        // 5. Convertir y retornar el DTO.
        return convertToDto(updatedJob);
    }

    /**
     * Deletes a job by its ID.
     * @param id The ID of the job to delete.
     */
    @Transactional
    public void deleteJob(Long id) {
        log.info("SERVICE - Intentando eliminar trabajo con ID: {}", id);

        // 1. Verificar si el trabajo existe antes de intentar eliminarlo.
        if (!jobRepository.existsById(id)) {
            throw new JobNotFoundException("Job not found with ID: " + id);
        }

        // 2. Eliminar el trabajo.
        jobRepository.deleteById(id);
        log.info("SERVICE - Trabajo con ID: {} eliminado exitosamente.", id);
    }

    private JobDTO convertToDto(Job job) {
        try {
            return new JobDTO(
                job.getId(),
                job.getTitle(),
                job.getLocation(),
                job.getCompanyProfile() != null ? job.getCompanyProfile().getId() : null,
                getCompanyNameSafe(job),
                job.getIsPaid(),
                job.getIsInternship(),
                job.getIsPartTime(),
                job.getDescription(),
                job.getJobDetails(),
                job.getRequirements(),
                job.getSalary(),
                job.getPostedAt(),
                job.getDeadline(),
                job.getPostedAgo(),
                job.getStatus() != null ? job.getStatus().name() : "ACTIVE"
            );
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo job ID " + job.getId(), e);
        }
    }

    private String getCompanyNameSafe(Job job) {
        try {
            if (job.getCompanyProfile() != null && job.getCompanyProfile().getName() != null) {
                return job.getCompanyProfile().getName();
            }
            return "Empresa no especificada";

        } catch (Exception e) {
            log.warn("Error obteniendo company name para job ID {}: {}", job.getId(), e.getMessage());
            return "Empresa no especificada";
        }
    }
}
