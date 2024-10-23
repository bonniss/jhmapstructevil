package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentGamma;

/**
 * Spring Data JPA repository for the NextPaymentGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentGammaRepository extends JpaRepository<NextPaymentGamma, Long>, JpaSpecificationExecutor<NextPaymentGamma> {}
