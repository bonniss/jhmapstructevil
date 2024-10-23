package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeGamma;

/**
 * Spring Data JPA repository for the NextEmployeeGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeGammaRepository extends JpaRepository<NextEmployeeGamma, Long>, JpaSpecificationExecutor<NextEmployeeGamma> {}
