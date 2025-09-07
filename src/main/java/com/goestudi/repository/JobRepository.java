package com.goestudi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goestudi.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	
	  /**
     * Busca trabajos cuyo título contenga la palabra clave, sin distinguir entre mayúsculas y minúsculas.
     * La lógica de la consulta es generada automáticamente por Spring.
     * @param keyword El término de búsqueda para el título.
     * @return Una lista de trabajos que coinciden.
     */
    List<Job> findByTitleContainingIgnoreCase(String keyword);

}
