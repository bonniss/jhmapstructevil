package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerAlpha;

/**
 * Spring Data JPA repository for the CustomerAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerAlphaRepository extends JpaRepository<CustomerAlpha, Long>, JpaSpecificationExecutor<CustomerAlpha> {}
