package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentAlpha;

/**
 * Spring Data JPA repository for the NextPaymentAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentAlphaRepository extends JpaRepository<NextPaymentAlpha, Long>, JpaSpecificationExecutor<NextPaymentAlpha> {}
