package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerVi;

/**
 * Spring Data JPA repository for the CustomerVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerViRepository extends JpaRepository<CustomerVi, Long>, JpaSpecificationExecutor<CustomerVi> {}
