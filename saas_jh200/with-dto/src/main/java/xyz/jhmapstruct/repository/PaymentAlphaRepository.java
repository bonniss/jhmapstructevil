package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentAlpha;

/**
 * Spring Data JPA repository for the PaymentAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentAlphaRepository extends JpaRepository<PaymentAlpha, Long>, JpaSpecificationExecutor<PaymentAlpha> {}
