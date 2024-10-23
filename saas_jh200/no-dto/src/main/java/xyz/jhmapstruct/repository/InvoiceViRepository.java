package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceVi;

/**
 * Spring Data JPA repository for the InvoiceVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceViRepository extends JpaRepository<InvoiceVi, Long>, JpaSpecificationExecutor<InvoiceVi> {}
