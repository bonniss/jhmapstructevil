package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeMi;

/**
 * Spring Data JPA repository for the NextEmployeeMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeMiRepository extends JpaRepository<NextEmployeeMi, Long>, JpaSpecificationExecutor<NextEmployeeMi> {}
