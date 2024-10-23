package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceGamma;

/**
 * Spring Data JPA repository for the NextInvoiceGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceGammaRepository extends JpaRepository<NextInvoiceGamma, Long>, JpaSpecificationExecutor<NextInvoiceGamma> {}
