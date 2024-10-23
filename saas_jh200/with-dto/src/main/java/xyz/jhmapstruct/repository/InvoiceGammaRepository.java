package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceGamma;

/**
 * Spring Data JPA repository for the InvoiceGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceGammaRepository extends JpaRepository<InvoiceGamma, Long>, JpaSpecificationExecutor<InvoiceGamma> {}
