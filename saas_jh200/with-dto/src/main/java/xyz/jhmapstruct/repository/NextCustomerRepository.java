package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomer;

/**
 * Spring Data JPA repository for the NextCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerRepository extends JpaRepository<NextCustomer, Long>, JpaSpecificationExecutor<NextCustomer> {}
