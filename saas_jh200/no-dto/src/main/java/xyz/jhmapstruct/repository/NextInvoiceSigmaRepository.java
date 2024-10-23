package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceSigma;

/**
 * Spring Data JPA repository for the NextInvoiceSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceSigmaRepository extends JpaRepository<NextInvoiceSigma, Long>, JpaSpecificationExecutor<NextInvoiceSigma> {}
