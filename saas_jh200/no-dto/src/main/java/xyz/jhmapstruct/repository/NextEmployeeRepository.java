package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployee;

/**
 * Spring Data JPA repository for the NextEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeRepository extends JpaRepository<NextEmployee, Long>, JpaSpecificationExecutor<NextEmployee> {}
