package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeTheta;

/**
 * Spring Data JPA repository for the NextEmployeeTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeThetaRepository extends JpaRepository<NextEmployeeTheta, Long>, JpaSpecificationExecutor<NextEmployeeTheta> {}
