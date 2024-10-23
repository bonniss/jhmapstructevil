package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeViVi;

/**
 * Spring Data JPA repository for the NextEmployeeViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeViViRepository extends JpaRepository<NextEmployeeViVi, Long>, JpaSpecificationExecutor<NextEmployeeViVi> {}
