package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeSigma;

/**
 * Spring Data JPA repository for the EmployeeSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeSigmaRepository extends JpaRepository<EmployeeSigma, Long>, JpaSpecificationExecutor<EmployeeSigma> {}
