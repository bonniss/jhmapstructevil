package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerGamma;

/**
 * Spring Data JPA repository for the CustomerGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerGammaRepository extends JpaRepository<CustomerGamma, Long>, JpaSpecificationExecutor<CustomerGamma> {}
