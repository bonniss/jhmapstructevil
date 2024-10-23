package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentTheta;

/**
 * Spring Data JPA repository for the NextPaymentTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentThetaRepository extends JpaRepository<NextPaymentTheta, Long>, JpaSpecificationExecutor<NextPaymentTheta> {}
