package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeVi;

/**
 * Spring Data JPA repository for the EmployeeVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeViRepository extends JpaRepository<EmployeeVi, Long> {}
