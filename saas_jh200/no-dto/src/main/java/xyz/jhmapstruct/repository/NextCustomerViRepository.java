package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerVi;

/**
 * Spring Data JPA repository for the NextCustomerVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerViRepository extends JpaRepository<NextCustomerVi, Long>, JpaSpecificationExecutor<NextCustomerVi> {}
