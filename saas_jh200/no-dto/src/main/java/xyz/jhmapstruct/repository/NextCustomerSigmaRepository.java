package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerSigma;

/**
 * Spring Data JPA repository for the NextCustomerSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerSigmaRepository extends JpaRepository<NextCustomerSigma, Long>, JpaSpecificationExecutor<NextCustomerSigma> {}
