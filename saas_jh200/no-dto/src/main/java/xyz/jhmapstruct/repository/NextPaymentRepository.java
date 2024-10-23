package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPayment;

/**
 * Spring Data JPA repository for the NextPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentRepository extends JpaRepository<NextPayment, Long>, JpaSpecificationExecutor<NextPayment> {}
