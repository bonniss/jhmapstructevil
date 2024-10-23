package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceMi;

/**
 * Spring Data JPA repository for the NextInvoiceMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceMiRepository extends JpaRepository<NextInvoiceMi, Long>, JpaSpecificationExecutor<NextInvoiceMi> {}
