package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceAlpha;

/**
 * Spring Data JPA repository for the NextInvoiceAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceAlphaRepository extends JpaRepository<NextInvoiceAlpha, Long>, JpaSpecificationExecutor<NextInvoiceAlpha> {}
