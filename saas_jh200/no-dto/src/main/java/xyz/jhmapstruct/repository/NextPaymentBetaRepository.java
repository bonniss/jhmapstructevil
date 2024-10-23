package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentBeta;

/**
 * Spring Data JPA repository for the NextPaymentBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentBetaRepository extends JpaRepository<NextPaymentBeta, Long>, JpaSpecificationExecutor<NextPaymentBeta> {}
