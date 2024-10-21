package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerMiMi;

/**
 * Spring Data JPA repository for the CustomerMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerMiMiRepository extends JpaRepository<CustomerMiMi, Long> {}
