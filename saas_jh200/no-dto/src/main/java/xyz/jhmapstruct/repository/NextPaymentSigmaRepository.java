package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentSigma;

/**
 * Spring Data JPA repository for the NextPaymentSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentSigmaRepository extends JpaRepository<NextPaymentSigma, Long>, JpaSpecificationExecutor<NextPaymentSigma> {}
