package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentMiMi;

/**
 * Spring Data JPA repository for the NextPaymentMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentMiMiRepository extends JpaRepository<NextPaymentMiMi, Long>, JpaSpecificationExecutor<NextPaymentMiMi> {}
