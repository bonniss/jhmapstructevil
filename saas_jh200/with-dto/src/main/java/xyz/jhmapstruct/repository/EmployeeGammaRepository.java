package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeGamma;

/**
 * Spring Data JPA repository for the EmployeeGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeGammaRepository extends JpaRepository<EmployeeGamma, Long>, JpaSpecificationExecutor<EmployeeGamma> {}
