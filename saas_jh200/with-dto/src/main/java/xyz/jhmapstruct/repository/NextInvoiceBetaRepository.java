package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceBeta;

/**
 * Spring Data JPA repository for the NextInvoiceBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceBetaRepository extends JpaRepository<NextInvoiceBeta, Long>, JpaSpecificationExecutor<NextInvoiceBeta> {}
