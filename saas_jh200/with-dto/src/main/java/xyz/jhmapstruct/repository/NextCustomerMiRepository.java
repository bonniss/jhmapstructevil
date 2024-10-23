package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerMi;

/**
 * Spring Data JPA repository for the NextCustomerMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerMiRepository extends JpaRepository<NextCustomerMi, Long>, JpaSpecificationExecutor<NextCustomerMi> {}
