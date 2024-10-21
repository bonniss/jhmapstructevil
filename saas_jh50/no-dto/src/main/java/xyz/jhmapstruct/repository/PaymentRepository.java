package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.Payment;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
