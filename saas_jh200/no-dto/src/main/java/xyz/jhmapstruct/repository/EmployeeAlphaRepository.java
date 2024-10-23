package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.EmployeeAlpha;

/**
 * Spring Data JPA repository for the EmployeeAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeAlphaRepository extends JpaRepository<EmployeeAlpha, Long>, JpaSpecificationExecutor<EmployeeAlpha> {}
