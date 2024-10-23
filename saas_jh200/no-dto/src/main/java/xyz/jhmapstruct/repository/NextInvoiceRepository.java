package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoice;

/**
 * Spring Data JPA repository for the NextInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceRepository extends JpaRepository<NextInvoice, Long>, JpaSpecificationExecutor<NextInvoice> {}
