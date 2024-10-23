package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerMiMi;

/**
 * Spring Data JPA repository for the NextCustomerMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerMiMiRepository extends JpaRepository<NextCustomerMiMi, Long>, JpaSpecificationExecutor<NextCustomerMiMi> {}
