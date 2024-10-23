package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceMiMi;

/**
 * Spring Data JPA repository for the NextInvoiceMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceMiMiRepository extends JpaRepository<NextInvoiceMiMi, Long>, JpaSpecificationExecutor<NextInvoiceMiMi> {}
