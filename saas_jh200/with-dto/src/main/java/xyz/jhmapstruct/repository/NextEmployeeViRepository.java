package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeVi;

/**
 * Spring Data JPA repository for the NextEmployeeVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeViRepository extends JpaRepository<NextEmployeeVi, Long>, JpaSpecificationExecutor<NextEmployeeVi> {}
