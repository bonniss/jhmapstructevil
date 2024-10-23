package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceMiMi;

/**
 * Spring Data JPA repository for the InvoiceMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceMiMiRepository extends JpaRepository<InvoiceMiMi, Long>, JpaSpecificationExecutor<InvoiceMiMi> {}
