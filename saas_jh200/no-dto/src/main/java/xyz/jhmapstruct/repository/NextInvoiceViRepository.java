package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceVi;

/**
 * Spring Data JPA repository for the NextInvoiceVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceViRepository extends JpaRepository<NextInvoiceVi, Long>, JpaSpecificationExecutor<NextInvoiceVi> {}
