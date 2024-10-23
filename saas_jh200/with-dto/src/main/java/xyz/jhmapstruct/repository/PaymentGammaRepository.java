package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentGamma;

/**
 * Spring Data JPA repository for the PaymentGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentGammaRepository extends JpaRepository<PaymentGamma, Long>, JpaSpecificationExecutor<PaymentGamma> {}
