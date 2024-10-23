package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeMi;

/**
 * Spring Data JPA repository for the EmployeeMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeMiRepository extends JpaRepository<EmployeeMi, Long>, JpaSpecificationExecutor<EmployeeMi> {}
