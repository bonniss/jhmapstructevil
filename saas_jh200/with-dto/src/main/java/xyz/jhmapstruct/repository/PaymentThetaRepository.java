package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentTheta;

/**
 * Spring Data JPA repository for the PaymentTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentThetaRepository extends JpaRepository<PaymentTheta, Long>, JpaSpecificationExecutor<PaymentTheta> {}
