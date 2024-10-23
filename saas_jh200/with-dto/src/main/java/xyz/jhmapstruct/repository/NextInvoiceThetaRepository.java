package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextInvoiceTheta;

/**
 * Spring Data JPA repository for the NextInvoiceTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextInvoiceThetaRepository extends JpaRepository<NextInvoiceTheta, Long>, JpaSpecificationExecutor<NextInvoiceTheta> {}
