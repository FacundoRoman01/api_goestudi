package com.goestudi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.goestudi.dto.JobDTO;
import com.goestudi.service.JobService;
//import com.goestudi.exception.JobNotFoundException; // Importamos la excepción

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Job API", description = "API para gestionar trabajos y ofertas de empleo (CRUD)")
public class JobController {
    
    private final JobService jobService;
    
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    // Método para CREAR un nuevo trabajo
    @Operation(summary = "Crear un nuevo trabajo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Trabajo creado exitosamente",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = JobDTO.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida (error de validación)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<JobDTO> createJob(@Valid @RequestBody JobDTO jobDTO) {
        JobDTO createdJob = jobService.createJob(jobDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
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
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @Valid @RequestBody JobDTO jobDTO) {
        JobDTO updatedJob = jobService.updateJob(id, jobDTO);
        return ResponseEntity.ok(updatedJob);
    }

    // Método para ELIMINAR un trabajo por su ID
    @Operation(summary = "Eliminar un trabajo por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Trabajo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
