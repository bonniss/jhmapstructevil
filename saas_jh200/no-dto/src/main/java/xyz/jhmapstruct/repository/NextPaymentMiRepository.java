package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentMi;

/**
 * Spring Data JPA repository for the NextPaymentMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentMiRepository extends JpaRepository<NextPaymentMi, Long>, JpaSpecificationExecutor<NextPaymentMi> {}
