package com.goestudi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goestudi.dto.JobDTO;
import com.goestudi.model.CompanyProfile;
import com.goestudi.model.User;
import com.goestudi.model.Job;
import com.goestudi.repository.JobRepository;
import com.goestudi.service.JobService;
import com.goestudi.service.CompanyProfileService;
import com.goestudi.exception.JobNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Job API", description = "API para gestionar trabajos y ofertas de empleo (CRUD)")
public class JobController {
    
    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    private final JobService jobService;
    private final CompanyProfileService companyProfileService;
    private final JobRepository jobRepository;

    public JobController(JobService jobService, CompanyProfileService companyProfileService, JobRepository jobRepository) {
        this.jobService = jobService;
        this.companyProfileService = companyProfileService;
        this.jobRepository = jobRepository;
    }
    
    // Método para CREAR un nuevo trabajo
    @Operation(summary = "Crear un nuevo trabajo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trabajo creado exitosamente",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = JobDTO.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida (error de validación)"),
        @ApiResponse(responseCode = "403", description = "Prohibido (sin perfil de empresa)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/meCompany")
    public ResponseEntity<JobDTO> createJob(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody JobDTO jobDTO) {
        log.info("CONTROLLER - Creando trabajo para el usuario: {}", userDetails.getUsername());
        
        User user = (User) userDetails;
        Optional<CompanyProfile> companyProfileOptional = companyProfileService.findByUser(user);

        if (companyProfileOptional.isEmpty()) {
            log.warn("CONTROLLER - Perfil de empresa no encontrado para el usuario: {}", user.getEmail());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        JobDTO createdJob = jobService.createJob(companyProfileOptional.get(), jobDTO);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }
    
    // Método para OBTENER todos los trabajos
    @Operation(summary = "Obtener una lista de trabajos con filtros opcionales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de trabajos obtenida exitosamente",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<JobDTO>> getJobs(
        @Parameter(description = "Palabra clave para buscar en el título o descripción")  
        @RequestParam(required = false) String keyword,
        @Parameter(description = "Ubicación del trabajo")  
        @RequestParam(required = false) String location,
        @Parameter(description = "Filtrar por pasantías")  
        @RequestParam(required = false) Boolean isInternship,
        @Parameter(description = "Filtrar por trabajos a tiempo parcial")  
        @RequestParam(required = false) Boolean isPartTime,
        @PageableDefault(size = 12) Pageable pageable) throws Exception {
        
        Page<JobDTO> jobPage = jobService.findJobsByFilters(keyword, location, isInternship, isPartTime, pageable);
        return ResponseEntity.ok(jobPage);
    }

    // Método para ACTUALIZAR un trabajo por su ID
    @Operation(summary = "Actualizar un trabajo existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trabajo actualizado exitosamente",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = JobDTO.class))),
        @ApiResponse(responseCode = "400", description = "ID o solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado"),
        @ApiResponse(responseCode = "403", description = "Prohibido (intento de modificar un trabajo no propio)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/meCompany/{id}")
    public ResponseEntity<JobDTO> updateJob(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody JobDTO jobDTO) {
        log.info("CONTROLLER - Actualizando trabajo con ID: {} para el usuario: {}", id, userDetails.getUsername());
        
        User user = (User) userDetails;
        Optional<CompanyProfile> companyProfileOptional = companyProfileService.findByUser(user);

        if (companyProfileOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            Optional<JobDTO> updatedJob = jobService.updateJob(companyProfileOptional.get(), id, jobDTO);
            return updatedJob.map(dto -> {
                log.info("CONTROLLER - Trabajo con ID: {} actualizado con éxito", id);
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }).orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        } catch (JobNotFoundException e) {
            log.warn("CONTROLLER - Trabajo no encontrado con ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Método para ELIMINAR un trabajo por su ID
    @Operation(summary = "Eliminar un trabajo por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Trabajo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado"),
        @ApiResponse(responseCode = "403", description = "Prohibido (intento de eliminar un trabajo no propio)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/meCompany/{id}")
    public ResponseEntity<Void> deleteJob(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        log.info("CONTROLLER - Eliminando trabajo con ID: {} para el usuario: {}", id, userDetails.getUsername());
        
        User user = (User) userDetails;
        Optional<CompanyProfile> companyProfileOptional = companyProfileService.findByUser(user);

        if (companyProfileOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            boolean isDeleted = jobService.deleteJob(companyProfileOptional.get(), id);
            if (isDeleted) {
                log.info("CONTROLLER - Trabajo con ID: {} eliminado con éxito", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (JobNotFoundException e) {
            log.warn("CONTROLLER - Trabajo no encontrado con ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Obtiene todos los trabajos publicados por la empresa autenticada.
     */
    @GetMapping("/company")
    public ResponseEntity<Page<JobDTO>> getMyJobs(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 12) Pageable pageable) {
        log.info("CONTROLLER - Buscando trabajos de la empresa para el usuario: {}", userDetails.getUsername());
        
        User user = (User) userDetails;
        Optional<CompanyProfile> companyProfileOptional = companyProfileService.findByUser(user);

        if (companyProfileOptional.isEmpty()) {
            log.warn("CONTROLLER - Perfil de empresa no encontrado para el usuario: {}", user.getEmail());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        Page<Job> jobPage = jobRepository.findByCompanyProfile(companyProfileOptional.get(), pageable);
        Page<JobDTO> dtoPage = jobPage.map(jobService::convertToDto);
        
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}
