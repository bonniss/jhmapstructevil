package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentBeta;

/**
 * Spring Data JPA repository for the PaymentBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentBetaRepository extends JpaRepository<PaymentBeta, Long>, JpaSpecificationExecutor<PaymentBeta> {}
