package com.goestudi.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.goestudi.model.CompanyProfile;
import com.goestudi.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>  {
	
	 /**
     * Busca todos los trabajos asociados a un perfil de empresa específico.
     * @param companyProfile El perfil de empresa por el que se debe filtrar.
     * @param pageable Objeto de paginación para limitar y ordenar los resultados.
     * @return Una página de objetos Job.
     */
    Page<Job> findByCompanyProfile(CompanyProfile companyProfile, Pageable pageable);

}
