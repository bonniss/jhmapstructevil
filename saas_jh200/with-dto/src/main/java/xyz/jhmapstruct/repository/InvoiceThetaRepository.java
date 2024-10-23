package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceTheta;

/**
 * Spring Data JPA repository for the InvoiceTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceThetaRepository extends JpaRepository<InvoiceTheta, Long>, JpaSpecificationExecutor<InvoiceTheta> {}
