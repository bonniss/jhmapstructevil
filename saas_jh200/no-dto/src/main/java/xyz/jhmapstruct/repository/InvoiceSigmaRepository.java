package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceSigma;

/**
 * Spring Data JPA repository for the InvoiceSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceSigmaRepository extends JpaRepository<InvoiceSigma, Long>, JpaSpecificationExecutor<InvoiceSigma> {}
