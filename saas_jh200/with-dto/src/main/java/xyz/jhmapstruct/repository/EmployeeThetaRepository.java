package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeTheta;

/**
 * Spring Data JPA repository for the EmployeeTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeThetaRepository extends JpaRepository<EmployeeTheta, Long>, JpaSpecificationExecutor<EmployeeTheta> {}
