package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerBeta;

/**
 * Spring Data JPA repository for the NextCustomerBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerBetaRepository extends JpaRepository<NextCustomerBeta, Long>, JpaSpecificationExecutor<NextCustomerBeta> {}
