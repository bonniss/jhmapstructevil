package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceBeta;

/**
 * Spring Data JPA repository for the InvoiceBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceBetaRepository extends JpaRepository<InvoiceBeta, Long>, JpaSpecificationExecutor<InvoiceBeta> {}
