package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerGamma;

/**
 * Spring Data JPA repository for the NextCustomerGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerGammaRepository extends JpaRepository<NextCustomerGamma, Long>, JpaSpecificationExecutor<NextCustomerGamma> {}
