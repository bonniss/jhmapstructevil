package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentMi;

/**
 * Spring Data JPA repository for the PaymentMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMiRepository extends JpaRepository<PaymentMi, Long>, JpaSpecificationExecutor<PaymentMi> {}
