package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.InvoiceMi;

/**
 * Spring Data JPA repository for the InvoiceMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceMiRepository extends JpaRepository<InvoiceMi, Long> {}
