package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerViVi;

/**
 * Spring Data JPA repository for the NextCustomerViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerViViRepository extends JpaRepository<NextCustomerViVi, Long>, JpaSpecificationExecutor<NextCustomerViVi> {}
