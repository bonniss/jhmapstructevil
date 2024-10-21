package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeViVi;

/**
 * Spring Data JPA repository for the EmployeeViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeViViRepository extends JpaRepository<EmployeeViVi, Long> {}
