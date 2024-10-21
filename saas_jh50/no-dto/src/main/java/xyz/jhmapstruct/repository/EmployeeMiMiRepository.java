package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeMiMi;

/**
 * Spring Data JPA repository for the EmployeeMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeMiMiRepository extends JpaRepository<EmployeeMiMi, Long> {}
