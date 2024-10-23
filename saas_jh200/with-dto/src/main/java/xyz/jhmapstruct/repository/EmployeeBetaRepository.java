package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeBeta;

/**
 * Spring Data JPA repository for the EmployeeBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeBetaRepository extends JpaRepository<EmployeeBeta, Long>, JpaSpecificationExecutor<EmployeeBeta> {}
