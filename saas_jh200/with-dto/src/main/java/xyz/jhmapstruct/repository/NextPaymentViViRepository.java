package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentViVi;

/**
 * Spring Data JPA repository for the NextPaymentViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentViViRepository extends JpaRepository<NextPaymentViVi, Long>, JpaSpecificationExecutor<NextPaymentViVi> {}
