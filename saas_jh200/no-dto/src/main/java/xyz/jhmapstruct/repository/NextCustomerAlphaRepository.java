package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCustomerAlpha;

/**
 * Spring Data JPA repository for the NextCustomerAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCustomerAlphaRepository extends JpaRepository<NextCustomerAlpha, Long>, JpaSpecificationExecutor<NextCustomerAlpha> {}
