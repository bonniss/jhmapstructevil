package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceViVi;

/**
 * Spring Data JPA repository for the NextInvoiceViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceViViRepository extends JpaRepository<NextInvoiceViVi, Long>, JpaSpecificationExecutor<NextInvoiceViVi> {}
