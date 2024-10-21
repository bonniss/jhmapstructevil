package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerViVi;

/**
 * Spring Data JPA repository for the CustomerViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerViViRepository extends JpaRepository<CustomerViVi, Long> {}
