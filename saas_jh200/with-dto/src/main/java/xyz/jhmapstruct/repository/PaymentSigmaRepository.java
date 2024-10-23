package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentSigma;

/**
 * Spring Data JPA repository for the PaymentSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentSigmaRepository extends JpaRepository<PaymentSigma, Long>, JpaSpecificationExecutor<PaymentSigma> {}
