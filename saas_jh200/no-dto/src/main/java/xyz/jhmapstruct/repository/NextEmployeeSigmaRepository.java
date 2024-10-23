package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeSigma;

/**
 * Spring Data JPA repository for the NextEmployeeSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeSigmaRepository extends JpaRepository<NextEmployeeSigma, Long>, JpaSpecificationExecutor<NextEmployeeSigma> {}
