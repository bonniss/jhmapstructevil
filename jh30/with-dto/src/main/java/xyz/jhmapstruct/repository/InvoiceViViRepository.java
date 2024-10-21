package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceViVi;

/**
 * Spring Data JPA repository for the InvoiceViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceViViRepository extends JpaRepository<InvoiceViVi, Long> {}
