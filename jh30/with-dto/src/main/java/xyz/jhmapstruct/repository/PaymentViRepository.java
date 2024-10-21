package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.PaymentVi;

/**
 * Spring Data JPA repository for the PaymentVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentViRepository extends JpaRepository<PaymentVi, Long> {}
