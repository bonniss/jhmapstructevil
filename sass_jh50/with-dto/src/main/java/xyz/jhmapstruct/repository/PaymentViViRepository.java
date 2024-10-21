package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentViVi;

/**
 * Spring Data JPA repository for the PaymentViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentViViRepository extends JpaRepository<PaymentViVi, Long> {}
