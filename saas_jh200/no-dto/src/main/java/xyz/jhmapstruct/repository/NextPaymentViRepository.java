package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextPaymentVi;

/**
 * Spring Data JPA repository for the NextPaymentVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextPaymentViRepository extends JpaRepository<NextPaymentVi, Long>, JpaSpecificationExecutor<NextPaymentVi> {}
