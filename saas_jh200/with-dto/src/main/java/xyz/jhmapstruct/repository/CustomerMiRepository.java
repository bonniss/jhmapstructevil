package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerMi;

/**
 * Spring Data JPA repository for the CustomerMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerMiRepository extends JpaRepository<CustomerMi, Long>, JpaSpecificationExecutor<CustomerMi> {}
