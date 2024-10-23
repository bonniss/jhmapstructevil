package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerTheta;

/**
 * Spring Data JPA repository for the NextCustomerTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerThetaRepository extends JpaRepository<NextCustomerTheta, Long>, JpaSpecificationExecutor<NextCustomerTheta> {}
