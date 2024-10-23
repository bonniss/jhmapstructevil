package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeMiMi;

/**
 * Spring Data JPA repository for the NextEmployeeMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeMiMiRepository extends JpaRepository<NextEmployeeMiMi, Long>, JpaSpecificationExecutor<NextEmployeeMiMi> {}
