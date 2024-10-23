package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerBeta;

/**
 * Spring Data JPA repository for the CustomerBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerBetaRepository extends JpaRepository<CustomerBeta, Long>, JpaSpecificationExecutor<CustomerBeta> {}
