package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceAlpha;

/**
 * Spring Data JPA repository for the InvoiceAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceAlphaRepository extends JpaRepository<InvoiceAlpha, Long>, JpaSpecificationExecutor<InvoiceAlpha> {}
