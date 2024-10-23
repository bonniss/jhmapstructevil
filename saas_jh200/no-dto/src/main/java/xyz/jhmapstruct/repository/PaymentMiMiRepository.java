package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentMiMi;

/**
 * Spring Data JPA repository for the PaymentMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMiMiRepository extends JpaRepository<PaymentMiMi, Long>, JpaSpecificationExecutor<PaymentMiMi> {}
